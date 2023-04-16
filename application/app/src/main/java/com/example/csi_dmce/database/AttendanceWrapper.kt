package com.example.csi_dmce.database

import android.util.Log
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

data class Attendance(
    @DocumentId
    var student_id: String?     = null,
    var event_id: String?       = null,
    var first: Boolean?         = false,
    var second: Boolean?        = false,
)

data class AttendanceSubDocument(
    @DocumentId
    var event_id: String?       = null,
    var first: Boolean?         = false,
    var second: Boolean?        = false,
)

class AttendanceWrapper {
    companion object {
        private val attendanceCollectionRef = FirebaseFirestore.getInstance().collection("attendance")

        private suspend fun getAttendanceDocumentReference(studentID: String, eventUUID: String) : DocumentReference {
            return attendanceCollectionRef.document(studentID)
                .collection("events")
                .document(eventUUID)
        }

        private suspend fun getAttendanceDocument(studentID: String, eventUUID: String) : DocumentSnapshot {
            return attendanceCollectionRef.document(studentID)
                .collection("events")
                .document(eventUUID)
                .get()
                .await()
        }

        suspend fun createAttendanceDocument(studentID: String, eventUUID: String) {
            Log.d("ATTEN_DOC", "${studentID}-${eventUUID}")

            val attendanceDoc = AttendanceSubDocument(event_id = eventUUID, first = false, second = false)

            attendanceCollectionRef.document(studentID).set(mapOf("dummy" to false)).await()
            attendanceCollectionRef.document(studentID).collection("events").document(eventUUID).set(attendanceDoc).await()
        }

        suspend fun getAttendanceObject(studentID: String, eventUUID: String) : Attendance? {
            val attendanceDocument = getAttendanceDocument(studentID, eventUUID)
            return attendanceDocument.toObject(Attendance::class.java)
        }

        suspend fun setQRAttendance(studentID: String, eventUUID: String, attendanceType: String, attendanceStatus: Boolean){
            val attendanceDocumentReference = getAttendanceDocumentReference(studentID, eventUUID)
            attendanceDocumentReference
                .update(attendanceType, attendanceStatus)
                .await()
        }

        suspend fun checkAttendanceStatus(studentID: String, eventUUID: String, attendanceType: String): Boolean? {
            val attendanceDocument = getAttendanceObject(studentID, eventUUID)
            return when(attendanceType) {
                "first" -> attendanceDocument?.first
                "second" -> attendanceDocument?.second
                else -> throw NotImplementedError("Invalid attendance type.")
            }
        }
    }
}
