package com.example.csi_dmce.database

import android.util.Log

object FirebaseDBWrapper {
    suspend fun commitDBAction() {
        var wrapper = StudentWrapper()
        Log.d("DB_USER RETURNED", wrapper.getStudentByID("2019FHCO106").toString())
        Log.d("DB_USER_DONE", "NO RESULT? :(")
    }
}