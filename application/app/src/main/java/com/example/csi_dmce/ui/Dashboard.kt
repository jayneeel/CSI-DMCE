package com.example.csi_dmce.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.events.EventListActivity
import com.example.csi_dmce.profile.Profile


class Dashboard: AppCompatActivity() {
    private lateinit var btnProfile : Button
    private lateinit var btnLogout: Button
    private lateinit var btnEvents: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        Log.d("CSI-UI", "Launching Dashboard")

        btnProfile = findViewById(R.id.btn_dashboard_profile)
        btnProfile.setOnClickListener {
            val eventIntent = Intent(this, Profile::class.java)
            startActivity(eventIntent)
        }

        btnEvents = findViewById(R.id.button_events)
        btnEvents.setOnClickListener {
            val intent = Intent(applicationContext, EventListActivity::class.java)
            startActivity(intent)
        }

        btnLogout = findViewById(R.id.btn_dashboard_logout)
        btnLogout.setOnClickListener {
            CsiAuthWrapper.deleteAuthToken(applicationContext)
            val intent = Intent(applicationContext, WelcomeActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }
    }
}