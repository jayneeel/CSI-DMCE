package com.example.csi_dmce.database

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


data class Attendance(
    @DocumentId
    var student_id: String?     = null,
    var event_id: String?       = null,
    var first: Boolean?         = null,
    var second: Boolean?         = null,
)

class AttendanceWrapper {
    companion object{
        private val attendanceCollectionRef = FirebaseFirestore.getInstance().collection("/attendance/event_id/students/student_id")

        suspend fun setSecond(attendance: Attendance):Void? {
            val attendanceDocumentRef = attendanceCollectionRef.document(attendance.student_id!!)
            return attendanceDocumentRef.set(attendance).await()
        }

        private fun getAttendanceDocument(attendanceCollectionRef: CollectionReference, studentId: String): DocumentReference {
            return attendanceCollectionRef.document(studentId)
        }

        suspend fun getattendance(studentId: String, eventID: String?) : Attendance? {
            val attendanceDocuments = AttendanceWrapper.attendanceCollectionRef
                .get()
                .await()

            for (attendanceDocument in attendanceDocuments) {
                if (attendanceDocument.get("student_id") == studentId) {
                    return attendanceDocument.toObject(Attendance::class.java)
                }
            }
            return null
        }


        }
    }
