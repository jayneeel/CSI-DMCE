package com.example.csi_admin.complaint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Complaint
import com.example.csi_dmce.database.ComplaintWrapper
import kotlinx.coroutines.runBlocking

class ComplaintsActivity : AppCompatActivity() {
    private lateinit var rcvComplaints: RecyclerView
    private lateinit var myAdapter: ComplaintAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaints)

        val complaints: List<Complaint> = runBlocking { ComplaintWrapper.getComplaintObjects() }

        rcvComplaints = findViewById(R.id.complaintRC)
        rcvComplaints.layoutManager = LinearLayoutManager(this)
        rcvComplaints.setHasFixedSize(true)

        myAdapter = ComplaintAdapter(complaints)
        rcvComplaints.adapter = myAdapter
    }
}