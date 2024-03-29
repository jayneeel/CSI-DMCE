package com.example.csi_dmce.database

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

data class Student(
    @DocumentId
    var student_id          : String?                   = null,
    var academic_year       : String?                   = null,
    var department          : String?                   = null,
    var division            : String?                   = null,
    var email               : String?                   = null,
    var email_id_verified   : Boolean?                  = null,
    var name                : String?                   = null,
    var phone_number        : Long?                     = null,
    val roll_number         : Int?                      = null,
    var events              : MutableList<String?>?     = null,
    var avatar_extension    : String?                   = null,
)

class StudentWrapper {
    companion object {
        private val studentCollectionRef = FirebaseFirestore.getInstance().collection("users")
        private val storageRef = FirebaseStorage.getInstance().reference.child("users")

        private fun getStudentDocument(studentCollectionRef: CollectionReference, studentId: String): DocumentReference {
            return studentCollectionRef.document(studentId)
        }

        /**
         * Add a student to the database.
         *
         * @param student the deserialized Student instance.
         * @return void
         */
        suspend fun addStudent(student: Student): Void? {
            val studentDocumentRef = studentCollectionRef.document(student.student_id!!)
            return studentDocumentRef.set(student).await()
        }

        /**
         * Read a student from the database.
         *
         * @param studentId the student ID of the student to be read.
         * @return the deserialized Student instance.
         */
        suspend fun getStudent(studentId: String): Student? {
            val studentDocument = getStudentDocument(studentCollectionRef, studentId).get().await()
            Log.d("STUDENT", studentDocument.reference.path.toString())
            return studentDocument.toObject(Student::class.java)!!
        }

        /**
         * Update a student in the database.
         *
         * @param oldStudent the student whose data is to be updated.
         * @param newStudent the new student instance containing updated details.
         */
        suspend fun updateStudent(oldStudent: Student, newStudent: Student) {
            // If the student IDs are different, then we have to delete the old document,
            if (oldStudent.student_id != newStudent.student_id) {
                getStudentDocument(studentCollectionRef, oldStudent.student_id!!).delete()
            }

            // And a add a new one
            addStudent(newStudent)
        }

        /**
         * Delete a student from the database.
         *
         * @param student the deserialized Student instance to be deleted.
         */
        fun deleteStudent(student: Student) {
            getStudentDocument(studentCollectionRef, student.student_id!!).delete()
        }

        suspend fun getStudentByEmail(email: String): Student? {
            val studentDocuments = studentCollectionRef
                .get()
                .await()

            for (studentDocument in studentDocuments) {
                if (studentDocument.get("email") == email) {
                    return studentDocument.toObject(Student::class.java)
                }
            }
            return null
        }

        suspend fun setStudentEmailIdVerificationStatus(studentId: String, emailIdVerified: Boolean) {
            val studentDocument = getStudentDocument(studentCollectionRef, studentId).get().await()
            Log.d("DB_EMAIL_VERIF_WRAPPER", studentDocument.toString())
            val studentObject: Student = studentDocument.toObject(Student::class.java)!!
            studentObject.email_id_verified = emailIdVerified
            studentCollectionRef.document(studentId).set(studentObject)
        }

        /*
        Storage stuff below this
         */

        suspend fun putStudentAvatar(studentObject: Student, imageUri: Uri): String {
            var avatarExtension = imageUri.lastPathSegment.toString()
            avatarExtension = avatarExtension.substring(avatarExtension.lastIndexOf(".") + 1)

            val studentId = studentObject.student_id!!

            storageRef
                .child("${studentId}/avatar.${avatarExtension}")
                .putFile(imageUri)
                .await()

            return avatarExtension
        }

        fun getStudentAvatarUrl(studentId: String, avatarExtension: String?, callback: (String?) -> Unit) {
            if (avatarExtension == null) {
                return callback(null)
            }

            storageRef
                .child("${studentId}/avatar.${avatarExtension}")
                .downloadUrl
                .addOnSuccessListener {
                    callback(it.toString())
                }.addOnFailureListener {
                    callback(null)
                }
        }
    }
}