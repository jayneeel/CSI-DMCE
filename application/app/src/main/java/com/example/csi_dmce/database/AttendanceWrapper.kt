package com.example.csi_dmce.database

import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

data class Attendance(
    @DocumentId
    var event_uuid: String?     = null,
    var first: Boolean?         = false,
    var second: Boolean?        = false,
)

class AttendanceWrapper {
    companion object{
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

        private suspend fun getAttendanceObject(studentID: String, eventUUID: String) : Attendance? {
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