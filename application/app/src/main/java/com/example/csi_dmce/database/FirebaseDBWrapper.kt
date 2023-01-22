package com.example.csi_dmce.database

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.runBlocking

object FirebaseDBWrapper {
    suspend fun commitDBAction() {
        var wrapper = StudentWrapper()
        Log.d("TAG", wrapper.getStudent("2019FHCO106").toString())
//        wrapper.addStudent(
//            Student(
//                "2019FHCO200",
//                "BE",
//                "CO",
//                "B",
//                "amitkulkarni2028@gmail.com",
//                "Amit",
//                7045086019,
//                2
//            )
//        )
    }
}