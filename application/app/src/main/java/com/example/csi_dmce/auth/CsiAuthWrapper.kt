package com.example.csi_dmce.auth

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.tasks.await

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.google.firebase.firestore.FirebaseFirestore

import com.example.csi_dmce.database.Student

class CsiAuthWrapper {
    companion object {
        private fun getCsiSharedPrefs(ctx: Context): SharedPreferences {
            return ctx.getSharedPreferences("csi_shared_prefs", Context.MODE_PRIVATE)
        }

        private val jwtDocumentRef = FirebaseFirestore
            .getInstance()
            .collection("credentials")
            .document("JWT_SECRET")

        private suspend fun generateAuthToken(student: Student, role: CSIRole): String {
            val secret: String = jwtDocumentRef
                .get()
                .await()
                .get("secret")
                .toString()

            val algorithm: Algorithm = Algorithm.HMAC256(secret)

            return JWT.create()
                .withClaim("student_id", student.student_id)
                .withClaim("role", role.role)
                .sign(algorithm)
        }

        suspend fun setAuthToken(ctx: Context, student: Student, role: CSIRole = CSIRole.ADMIN): Boolean {
            val token: String = generateAuthToken(student, role)
            return getCsiSharedPrefs(ctx)
                .edit()
                .putString("auth_token", token)
                .commit()
        }

        fun deleteAuthToken(ctx: Context): Boolean {
            return getCsiSharedPrefs(ctx)
                .edit()
                .remove("auth_token")
                .commit()
        }

        fun parseAuthToken(ctx: Context): DecodedJWT {
            val token = getCsiSharedPrefs(ctx)
                .getString("auth_token", null)

            return JWT.decode(token)
        }

        fun getRoleFromToken(ctx: Context): CSIRole {
            val token = getCsiSharedPrefs(ctx)
                .getString("auth_token", null)

            val decodedToken = JWT.decode(token)
            return tokenRoleToCsiRole(decodedToken.getClaim("role").asString())
        }


        fun isAuthenticated(ctx: Context): Boolean {
            return getCsiSharedPrefs(ctx).getString("auth_token", null) != null
        }
    }
}