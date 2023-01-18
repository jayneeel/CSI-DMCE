package com.example.csi_dmce.database

import com.example.csi_dmce.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseDBWrapper {
    fun getDBReference(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    fun getDBRootReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }
}