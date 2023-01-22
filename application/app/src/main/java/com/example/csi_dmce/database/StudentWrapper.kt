package com.example.csi_dmce.database

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class RSVPEvent(
    var id          : String? = null,
    val attending   : Boolean? = null,
)

data class Student(
    var student_id      : String?       = null,
    val academic_year   : String?       = null,
    val department      : String?       = null,
    val division        : String?       = null,
    val email           : String?       = null,
    val name            : String?       = null,
    val phone_number    : Long?         = null,
    val roll_number     : Int?          = null,
    var events          : List<RSVPEvent?>?  = null,
)

class StudentWrapper {
    val userCollectionRef = FirebaseFirestore.getInstance().collection("users")

    private suspend fun getStudentEvents(doc: DocumentSnapshot): List<RSVPEvent> {
        val events: MutableList<RSVPEvent> = mutableListOf()
        val what = doc.reference.collection("events")
            .document("title-timestamp")
        val event = what.get().await().toObject(RSVPEvent::class.java)
        events.add(event!!)
        return events
    }

    suspend fun getStudent(userId: String): Student? {
        val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)
        val doc = userRef.get().await()
        val student = doc.toObject(Student::class.java)!!
        student.student_id = doc.id
        student.events = getStudentEvents(doc)

        return student
    }

    suspend fun addStudent(student: Student): Void? {
        val eventRef = userCollectionRef.document(student.student_id!!)
        return eventRef.set(student).await()
    }
}