package com.example.csi_admin.complaint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_admin.R
import com.google.firebase.firestore.*

class ComplaintsActivity : AppCompatActivity() {
    lateinit var recycler: RecyclerView
    private lateinit var complaintArrayList: ArrayList<Complaint>
    private lateinit var myAdapter: ComplaintAdapter
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaints)
        recycler = findViewById(R.id.complaintRC)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

        complaintArrayList = arrayListOf()

        myAdapter = ComplaintAdapter(complaintArrayList)
        recycler.adapter = myAdapter
        EventChangerListener()
    }

    private fun EventChangerListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("complaints").addSnapshotListener(object : EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error !=null){
                    Log.e("Firestore Error",error.message.toString())
                    return
                }
                for ( dc: DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        complaintArrayList.add(dc.document.toObject(Complaint::class.java))
                    }
                }
                myAdapter.notifyDataSetChanged()
            }

        })
    }
}