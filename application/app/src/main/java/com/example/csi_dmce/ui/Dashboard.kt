package com.example.csi_dmce.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.auth.EmailKind
import com.example.csi_dmce.auth.EmailService
import com.example.csi_dmce.events.EventViewActivity
import com.example.csi_dmce.profile.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class Dashboard: AppCompatActivity() {
    private lateinit var btnEmail: Button
    private lateinit var btn_profile: Button
    private lateinit var btn_events: Button
    private lateinit var btn_calendar: Button
    private lateinit var btn_logout: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        Log.d("CSI-UI", "Launching Dashboard")

        btn_profile = findViewById(R.id.btn_dashboard_profile)
        btn_profile.setOnClickListener {
            val eventIntent = Intent(this, Profile::class.java)
            startActivity(eventIntent)
        }


        Log.i("CSI-UI", "Setting Events onClick")
        btn_events = findViewById(R.id.btn_dashboard_events)
        btn_events.setOnClickListener {
            Log.i("CSI-UI", "Clicked on events")
            val eventIntent = Intent(this, EventViewActivity::class.java)
            startActivity(eventIntent)
        }

        btn_logout = findViewById(R.id.btn_dashboard_logout)
        btn_logout.setOnClickListener {
            CsiAuthWrapper.deleteAuthToken(applicationContext)
            val intent = Intent(applicationContext, WelcomeActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }
    }
}