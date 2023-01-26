package com.example.csi_dmce.database

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class StudentAuth(
    @DocumentId
    val email: String?          = null,
    val password_hash: String?  = null,
)

class StudentAuthWrapper {
    companion object {
        private val authCollectionRef = FirebaseFirestore.getInstance().collection("auth")

        suspend fun getByEmail(email: String): StudentAuth? {
            val studentDocument = authCollectionRef
                .document(email)
                .get()
                .await()

            return studentDocument.toObject(StudentAuth::class.java)
        }

        suspend fun addStudentAuth(studentAuth: StudentAuth) {
            authCollectionRef
                .document(studentAuth.email!!)
                .set(studentAuth)
                .await()
        }

        /**
         * Given an email and a SHA-256 password hash, check if the credentials match with the
         * ones in the database.
         *
         * @param email the email of the student.
         * @param password_hash the SHA-256 of the password entered by the user.
         * @return a `Student` object if the student authenticates with valid credentials.
         */
        suspend fun checkStudentCredentials(email: String, password_hash: String, callback: (Student?) -> Unit) {
            val student: StudentAuth? = getByEmail(email)
            if (student?.password_hash == password_hash) {
                return callback(StudentWrapper.getStudentByEmail(email))
            }

            return callback(null)
        }
    }
}