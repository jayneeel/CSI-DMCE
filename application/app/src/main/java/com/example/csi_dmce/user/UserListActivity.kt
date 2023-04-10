package com.example.csi_admin.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_admin.R
import com.example.csi_admin.complaint.Complaint
import com.example.csi_admin.complaint.ComplaintAdapter
import com.google.firebase.firestore.*

class UserListActivity : AppCompatActivity() {
    lateinit var recycler: RecyclerView
    private lateinit var usersArrayList: ArrayList<RegisteredUsers>
    private lateinit var myAdapter: RegisteredUserAdapter
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        recycler = findViewById(R.id.userRecyclerView)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

        usersArrayList = arrayListOf()

        myAdapter = RegisteredUserAdapter(usersArrayList)
        recycler.adapter = myAdapter
        EventChangerListener()
        
    }

    private fun EventChangerListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("users").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error !=null){
                    Log.e("Firestore Error",error.message.toString())
                    return
                }
                for ( dc: DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        usersArrayList.add(dc.document.toObject(RegisteredUsers::class.java))
                    }
                }
                myAdapter.notifyDataSetChanged()
            }

        })
    }
}