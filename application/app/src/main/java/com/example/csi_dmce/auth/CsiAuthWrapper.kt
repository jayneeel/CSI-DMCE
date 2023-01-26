package com.example.csi_dmce.auth

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.tasks.await

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.firebase.firestore.FirebaseFirestore

import com.example.csi_dmce.database.Student

class CsiAuthWrapper {
    companion object {
        private fun getCsiSharedPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences("csi_shared_prefs", Context.MODE_PRIVATE)
        }

        private val jwtDocumentRef = FirebaseFirestore
            .getInstance()
            .collection("JWT_SECRET")
            .document("JWT_SECRET")

        private suspend fun generateAuthToken(student: Student, role: CSIRole): String {
            val secret: String = jwtDocumentRef
                .get()
                .await()
                .get("SECRET")
                .toString()

            val algorithm: Algorithm = Algorithm.HMAC256(secret)

            return JWT.create()
                .withClaim("student_id", student.student_id)
                .withClaim("role", role.role)
                .sign(algorithm)
        }

        suspend fun setAuthToken(student: Student, role: CSIRole = CSIRole.USER, context: Context): Boolean {
            val token: String = generateAuthToken(student, role)
            return getCsiSharedPrefs(context)
                .edit()
                .putString("auth_token", token)
                .commit()
        }

        fun deleteAuthToken(context: Context): Boolean {
            return getCsiSharedPrefs(context)
                .edit()
                .remove("auth_token")
                .commit()
        }

        fun isAuthenticated(context: Context): Boolean {
            return getCsiSharedPrefs(context).getString("auth_token", null) != null
        }
    }
}