package com.example.csi_dmce.database

import android.util.Log
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class StudentAuth(
    @DocumentId
    val email: String?                              = null,
    var password_hash: String?                      = null,
    val email_verification: HashMap<String, Any>?   = null,
    val forgot_password: HashMap<String, Any>?      = null
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
         * @param passwordHash the SHA-256 of the password entered by the user.
         * @return a `Student` object if the student authenticates with valid credentials.
         */
        suspend fun checkStudentCredentials(email: String, passwordHash: String, callback: (Student?) -> Unit) {
            Log.d("DB_AUTH", email.toString() + " AND HASH " + passwordHash.toString())
            val student: StudentAuth? = getByEmail(email)
            Log.d("DB_AUTH", student.toString())
            if (student?.password_hash == passwordHash) {
                return callback(StudentWrapper.getStudentByEmail(email))
            }

            return callback(null)
        }
        suspend fun EmailVerificationWrapper(emailId: String): HashMap<String, Any> {
            val studentAuthRef: DocumentSnapshot? = authCollectionRef
                .document(emailId)
                .get().await()

            val studentAuthObject = studentAuthRef!!.toObject(StudentAuth::class.java)!!
            return studentAuthObject.email_verification!!
        }

        suspend fun SetEmailVerificationStatus(emailId: String, verificationStatus: String): Boolean {
            val studentAuthRef: DocumentSnapshot? = authCollectionRef
                .document(emailId)
                .get().await()

            val studentAuthObject = studentAuthRef!!.toObject(StudentAuth::class.java)!!
            studentAuthObject.email_verification?.set("verification_status", verificationStatus)
            authCollectionRef.document(emailId).set(studentAuthObject).await()
        }

        suspend fun ForgotPasswordWrapper(emailId: String): HashMap<String, Any> {
            val studentAuthRef: DocumentSnapshot? = authCollectionRef
                .document(emailId)
                .get().await()

            val studentAuthObject = studentAuthRef!!.toObject(StudentAuth::class.java)!!
            return studentAuthObject.forgot_password!!
        }

        suspend fun SetPasswordWrapper(emailId: String, passwordHash: String) {
            val studentAuthRef: DocumentSnapshot? = authCollectionRef
                .document(emailId)
                .get().await()

            val studentAuthObject = studentAuthRef!!.toObject(StudentAuth::class.java)!!
            studentAuthObject.password_hash = passwordHash
            authCollectionRef.document(emailId).set(studentAuthObject).await()
        }
    }
}