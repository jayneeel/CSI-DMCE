package com.example.csi_dmce.database

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class Expense(
    @DocumentId
    var expenseId                  :String?    =null,
    var student_id                 :String?    =null,
    var associated_event           :String?    =null,
    var date_of_event              :Long?      =null,
    var cost                       :String?    =null,
    var upi_id                     :String?    =null
)

class ExpensesWrapper {
    companion object{
        private val expenseCollectionRef = FirebaseFirestore.getInstance().collection("expenses")

        private fun getExpensesDocument(expenseCollectionRef: CollectionReference, expenseID: String): DocumentReference {
            return expenseCollectionRef.document(expenseID)
        }

        suspend fun getExpensesObjects(): List<Expense> {
            val expenseObjects = mutableListOf<Expense>()

            val expenseDocuments = expenseCollectionRef.get().await()
            for(expenseDocument in expenseDocuments){
                expenseObjects.add(expenseDocument.toObject(Expense::class.java))
            }

            return expenseObjects
        }
    }
}