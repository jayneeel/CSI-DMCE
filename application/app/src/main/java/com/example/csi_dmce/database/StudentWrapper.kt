package com.example.csi_dmce.database

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class Student(
    @DocumentId
    var student_id      : String?         = null,
    val academic_year   : String?         = null,
    val department      : String?         = null,
    val division        : String?         = null,
    val email           : String?         = null,
    val name            : String?         = null,
    val phone_number    : Long?           = null,
    val roll_number     : Int?            = null,
    var events          : List<String?>?  = null,
)

class StudentWrapper {
    val studentCollectionRef = FirebaseFirestore.getInstance().collection("users")

    private fun getStudentDocument(studentCollectionRef: CollectionReference, studentId: String): DocumentReference {
        return studentCollectionRef.document(studentId)
    }

    private suspend fun addStudent(student: Student): Void? {
        val studentDocumentRef = studentCollectionRef.document(student.student_id!!)
        return studentDocumentRef.set(student).await()
    }

    suspend fun getStudent(studentId: String): Student? {
        val studentDocument = getStudentDocument(studentCollectionRef, studentId).get().await()
        return studentDocument.toObject(Student::class.java)!!
    }

    suspend fun updateStudent(oldStudent: Student, newStudent: Student) {
        // If the student IDs are different, then we have to delete the old document,
        if (oldStudent.student_id != newStudent.student_id) {
            getStudentDocument(studentCollectionRef, oldStudent.student_id!!).delete()
        }

        // And a add a new one
        addStudent(newStudent)
    }

    fun deleteStudent(student: Student) {
        getStudentDocument(studentCollectionRef, student.student_id!!).delete()
    }
}