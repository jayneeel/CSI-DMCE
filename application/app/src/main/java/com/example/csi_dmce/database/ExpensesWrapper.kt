package com.example.csi_dmce.database

import com.example.csi_dmce.utils.Helpers
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

data class Expense(
    @DocumentId
    var expenseId                  :String?    =null,
    var student_id                 :String?    =null,
    var associated_event           :String?    =null,
    var date_of_event              :Long?      =null,
    var cost                       :String?    =null,
    var upi_id                     :String?    =null,
    var approval_status            :String?    =null,
)

enum class ApprovalStatus(val status: String) {
    Pending("pending"),
    Rejected("rejected"),
    Approved("approved")
}

class ExpensesWrapper {
    companion object{
        private val expenseCollectionRef = FirebaseFirestore.getInstance().collection("expenses")

        private fun getExpensesDocument(expenseID: String): DocumentReference {
            return expenseCollectionRef.document(expenseID)
        }

        private suspend fun setExpensesDocument(expenseObject: Expense) {
            val expenseId: String = expenseObject.expenseId ?: {
                expenseObject.student_id + "-" + Helpers.generateUnixTimestampFromDate(Date()).toString()
            }.toString()

            expenseObject.expenseId = expenseId

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
            setExpensesDocument(expenseObject)
        }
    }
}