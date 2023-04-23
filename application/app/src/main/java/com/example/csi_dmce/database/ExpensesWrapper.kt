package com.example.csi_dmce.database

import android.net.Uri
import com.example.csi_dmce.utils.Helpers
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.Date
import kotlin.math.exp

data class Expense(
    @DocumentId
    var expenseId                  :String?    =null,
    var student_id                 :String?    =null,
    var associated_event           :String?    =null,
    var topic                      :String?    =null,
    var date_of_event              :Long?      =null,
    var cost                       :String?    =null,
    var upi_id                     :String?    =null,
    var approval_status            :String?    =null,
    var proof_extension            :String?    =null,
)

enum class ApprovalStatus(val status: String) {
    Pending("pending"),
    Rejected("rejected"),
    Approved("approved")
}

class ExpensesWrapper {
    companion object{
        private val expenseCollectionRef = FirebaseFirestore.getInstance().collection("expenses")
        val expenseStorageRef = FirebaseStorage.getInstance().reference.child("expenses")

        private fun getExpensesDocument(expenseID: String): DocumentReference {
            return expenseCollectionRef.document(expenseID)
        }

        suspend fun addExpense(expenseObject: Expense, imageUri: Uri? = null) {
            val expenseId: String = expenseObject.expenseId ?: run {
                expenseObject.student_id + "-" + Helpers.generateUnixTimestampFromDate(Date())
                    .toString()
            }

            expenseObject.expenseId = expenseId

            if (imageUri != null) {
                expenseObject.proof_extension = uploadProofOfExpense(imageUri, expenseId)
            }

            expenseCollectionRef
                .document(expenseId)
                .set(expenseObject)
                .await()
        }

        suspend fun getExpensesObjects(): List<Expense> {
            val expenseObjects = mutableListOf<Expense>()

            val expenseDocuments = expenseCollectionRef.get().await()
            for(expenseDocument in expenseDocuments){
                expenseObjects.add(expenseDocument.toObject(Expense::class.java))
            }

            return expenseObjects
        }

        suspend fun setApprovalStatus(expenseObject: Expense, approvalStatus: ApprovalStatus) {
            expenseObject.approval_status = approvalStatus.status
            addExpense(expenseObject)
        }

        suspend fun uploadProofOfExpense(imageUri: Uri, expenseId: String): String {
            var proofExtension = imageUri.lastPathSegment.toString()
            proofExtension = proofExtension.substring(proofExtension.lastIndexOf(".") + 1)

            val imageRef = expenseStorageRef.child("${expenseId}/proof.${proofExtension}")
            imageRef.putFile(imageUri).await()

            return proofExtension
        }
    }
}