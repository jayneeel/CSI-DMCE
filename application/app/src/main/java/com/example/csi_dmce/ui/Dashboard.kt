package com.example.csi_dmce.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.database.StudentWrapper
import com.example.csi_dmce.events.EventListActivity
import com.example.csi_dmce.profile.Profile
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.runBlocking


class Dashboard: AppCompatActivity() {
    private lateinit var btnProfile : FloatingActionButton
    private lateinit var btnLogout: FloatingActionButton
    private lateinit var btnEvents: Button
    private lateinit var tvDashName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val studentObject = runBlocking { StudentWrapper.getStudent(CsiAuthWrapper.getStudentId(applicationContext)) }

        tvDashName = findViewById(R.id.text_view_dashboard_name)
        tvDashName.setText(studentObject?.name)

        btnProfile = findViewById(R.id.button_dashboard_profile)
        btnProfile.setOnClickListener {
            val eventIntent = Intent(this, Profile::class.java)
            startActivity(eventIntent)
        }

        btnEvents = findViewById(R.id.button_dashboard_events)
        btnEvents.setOnClickListener {
            val intent = Intent(applicationContext, EventListActivity::class.java)
            startActivity(intent)
        }

        btnLogout = findViewById(R.id.button_dashboard_logout)
        btnLogout.setOnClickListener {
            CsiAuthWrapper.deleteAuthToken(applicationContext)
            val intent = Intent(applicationContext, WelcomeActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }
    }
}