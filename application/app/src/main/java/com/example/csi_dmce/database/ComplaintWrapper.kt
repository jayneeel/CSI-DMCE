package com.example.csi_dmce.database

import com.example.csi_dmce.utils.Helpers
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

data class Complaint(
    @DocumentId
    var complaintId     :String? = null,
    var student_id      :String? = null,
    var student_name    :String? = null,
    var subject         :String? = null,
    var description     :String? = null,
    var is_resolved     :Boolean? = null,
    var reply           :String? = null,
    var registered_at   :Long? = null,
    var resolved_at     :Long? = null,
)

class ComplaintWrapper {
    companion object {
        private val complaintCollectionRef = FirebaseFirestore.getInstance().collection("complaints")

        private fun getComplaintDocument(complaintId: String): DocumentReference {
            return complaintCollectionRef.document(complaintId)
        }

        suspend fun addComplaint(complaintObject: Complaint) {
            val complaintId: String = complaintObject.complaintId ?: run {
                complaintObject.student_id + "-" + Helpers.generateUnixTimestampFromDate(Date())
                    .toString()
            }

            complaintObject.complaintId = complaintId

            complaintCollectionRef
                .document(complaintId)
                .set(complaintObject)
                .await()
        }

        suspend fun getComplaintObjects(): List<Complaint> {
            val complaintObjects = mutableListOf<Complaint>()

            val complaintDocuments = complaintCollectionRef.get().await()
            for(complainDocument in complaintDocuments){
                complaintObjects.add(complainDocument.toObject(Complaint::class.java))
            }

            return complaintObjects
        }
    }
}
