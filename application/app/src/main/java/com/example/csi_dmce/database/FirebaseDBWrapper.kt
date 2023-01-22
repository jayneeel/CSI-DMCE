package com.example.csi_dmce.database

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.runBlocking

object FirebaseDBWrapper {
    suspend fun commitDBAction() {
        var studentWrapper = StudentWrapper()
        val eventWrapper = EventWrapper()
        var user: Student? = null
        var event: DocumentReference? = null

        eventWrapper.addEvent()
    }
}