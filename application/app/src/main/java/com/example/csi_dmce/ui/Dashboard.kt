package com.example.csi_dmce.ui

import android.app.Dialog
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
    private lateinit var btn_profile: Button
    private lateinit var btn_logout: Button
    private lateinit var btn_event1: Button
    private lateinit var btn_event2: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        Log.d("CSI-UI", "Launching Dashboard")

        btn_profile = findViewById(R.id.btn_dashboard_profile)
        btn_profile.setOnClickListener {
            val eventIntent = Intent(this, Profile::class.java)
            startActivity(eventIntent)
        }

        btn_event1 = findViewById(R.id.btn_event_1)
        btn_event1.setOnClickListener {
            val intent = Intent(applicationContext, EventViewActivity::class.java)
            intent.putExtra("event_id", "WoamCT-2839823829")
            startActivity(intent)
        }

        btn_event2 = findViewById(R.id.btn_event_2)
        btn_event2.setOnClickListener {
            val intent = Intent(applicationContext, EventViewActivity::class.java)
            intent.putExtra("event_id", "DCF-1674498731")
            startActivity(intent)
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