package com.example.csi_dmce.database

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val academic_year: String? = null,
    val department: String? = null,
    val division: String? = null,
    val email_id: String? = null,
    val name: String? = null,
    val password_hash: String? = null,
    val phone_number: Int? = null,
    val roll_number: Int? = null,
)

class UserProfileWrapper {
    val dbRef = FirebaseDBWrapper.getDBRootReference().child("Users")

    fun convertSnapshotToUser(snapshot: DataSnapshot): User? {
        return snapshot.getValue(User::class.java)
    }

    fun CreateNewUser(user: User) {
        dbRef.setValue(user)
    }

    fun getAllUsers(): List<User> {
        val users: MutableList<User> = mutableListOf()

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val user: User = convertSnapshotToUser(childSnapshot)!!
                    users.add(user)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO: Do something better than log
                Log.i("FIREBASE-USERS", "DB ERROR. FAILED WHILE GETTING ALL USERS.")
            }
        })

        return users
    }
}
