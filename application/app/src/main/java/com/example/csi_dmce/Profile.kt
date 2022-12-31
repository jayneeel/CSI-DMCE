package com.example.csi_dmce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private  lateinit var newRecyclerView: RecyclerView
private  lateinit var newArrayList: ArrayList<ProfileEvents>
lateinit var title_Heading : ArrayList<String>
lateinit var Description : ArrayList<String>
class Profile: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val profile_button = findViewById<Button>(R.id.button_profile)
        profile_button.setOnClickListener(View.OnClickListener {  val i = Intent(this,edit_profile::class.java)
            startActivity(i) })
        val name = findViewById<TextView>(R.id.name_view)
        val branch = findViewById<TextView>(R.id.branch_view)
        val roll = findViewById<TextView>(R.id.rollno_view)
        val email = findViewById<TextView>(R.id.email_view)
        val password = findViewById<TextView>(R.id.password_view)
        val name_view = intent.getStringExtra("name 1801")
        val branch_view = intent.getStringExtra("branch 1801")
        val roll_view = intent.getStringExtra("roll 1801")
        val email_view = intent.getStringExtra("email 1801")
        val password_view = intent.getStringExtra("passsword 1801")
        name.setText(name_view)
        branch.setText(branch_view)
        roll.setText(roll_view)
        email.setText(email_view)
        password.setText(password_view)
        title_Heading = arrayListOf(
        )
        Description = arrayListOf(
        )
        newRecyclerView = findViewById(R.id.recyclerview)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        newArrayList = arrayListOf<ProfileEvents>()
        getUserdata()
    }
    private fun getUserdata(){
        for (i in title_Heading.indices){
            val Profile_Events = ProfileEvents(title_Heading[i], Description[i])
            newArrayList.add(Profile_Events)
        }
        newRecyclerView.adapter = ProfileEventsAdapter(newArrayList)
    }
}