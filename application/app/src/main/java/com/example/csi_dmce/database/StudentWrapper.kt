package com.example.csi_dmce.database

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

data class Event(
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
    var events          : List<Event?>?  = null,
)

class StudentWrapper {
    suspend fun getStudentByID(student_id: String?): Student? {
        val userRef = FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(student_id!!)

        var student: Student? = null

        userRef.get().addOnSuccessListener { document ->
            if (document != null) {
                student = document.toObject(Student::class.java).apply {
                    this!!.student_id = document.id
                }

                val eventsRef = document
                    .reference
                    .collection("events")

                runBlocking {
                    eventsRef.get()
                        .addOnSuccessListener { eventsSnapshot ->
                            val events = eventsSnapshot.map { eventSnapshot ->
                                eventSnapshot.toObject(Event::class.java).apply { id = eventSnapshot.id }
                            }
                            student?.events = events

                            Log.d("DB_USER_FINAL", student.toString())
                        }.await()
                }

            } else {
                Log.d("DB_USER", "No such document")
            }
        }.await()

        Log.d("DB_USER RETURNING", student.toString())

        return student
    }
}