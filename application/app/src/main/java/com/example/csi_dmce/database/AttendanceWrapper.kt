package com.example.csi_dmce.database

import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

data class Attendance(
    @DocumentId
    var student_id: String?     = null,
    var event_id: String?       = null,
    var first: Boolean?         = false,
    var second: Boolean?        = false,
)

class AttendanceWrapper {
    companion object{
        private val attendanceCollectionRef = FirebaseFirestore.getInstance().collection("attendance")

        fun getStudentDocumentRef(eventID: String?, studentId: String?): DocumentReference {
            return attendanceCollectionRef
                .document(eventID!!)
                .collection("students")
                .document(studentId!!)
        }

        suspend fun setSecond(eventID: String?, studentId: String?) :Boolean {
            val attendanceDocumentRef = getStudentDocumentRef(eventID, studentId)
            val attendanceObject: Attendance? = attendanceDocumentRef
                .get()
                .await()
                .toObject(Attendance::class.java)

            attendanceObject!!.second = true
            return true
        }

        suspend fun getAttendance(studentId: String, eventID: String?) : Attendance? {
            val studentDocumentRef: DocumentReference = getStudentDocumentRef(studentId, eventID)
            return studentDocumentRef.get().await().toObject(Attendance::class.java)
        }
    }
}
