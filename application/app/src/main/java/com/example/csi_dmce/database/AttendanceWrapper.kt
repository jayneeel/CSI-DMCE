package com.example.csi_dmce.database

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

data class Attendance(
    @DocumentId
    var student_id: String?     = null,
    var event_uuid: String?       = null,
    var first: Boolean?         = false,
    var second: Boolean?        = false,
)

class AttendanceWrapper {
    companion object{
        private val attendanceCollectionRef = FirebaseFirestore.getInstance().collection("attendance")

        private fun getAttendanceDocument(attendanceCollectionRef: CollectionReference, studentID: String, eventUUID: String) : DocumentReference{
            return attendanceCollectionRef.document(studentID)
                .collection("events")
                .document(eventUUID)
        }

        suspend fun getAttendance(studentID: String, eventUUID: String) : Attendance? {
            val attendanceDocument = getAttendanceDocument(attendanceCollectionRef, studentID, eventUUID).get().await()
            return attendanceDocument.toObject(Attendance::class.java)
        }

        suspend fun setFirstQRAttendance(studentID: String, eventUUID: String, scanningStatus1: Boolean){
            attendanceCollectionRef
                .document(studentID)
                .collection("events")
                .document(eventUUID)
                .update("first", scanningStatus1)
                .await()
        }

        suspend fun setSecondQRAttendance(studentID: String, eventUUID: String, scanningStatus2: Boolean){
            attendanceCollectionRef
                .document(studentID)
                .collection("events")
                .document(eventUUID)
                .update("second", scanningStatus2)
                .await()
        }

        suspend fun checkAttendanceStatus1(studentID: String, eventUUID: String): Boolean? {
            val attendanceDocument = getAttendance(studentID, eventUUID)
            return attendanceDocument?.first
        }

        suspend fun checkAttendanceStatus2(studentID: String, eventUUID: String): Boolean? {
            val attendanceDocument = getAttendance(studentID, eventUUID)
            return attendanceDocument?.second
        }

    }
}
