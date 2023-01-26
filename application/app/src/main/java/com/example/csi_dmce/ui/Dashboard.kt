package com.example.csi_dmce.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.MainActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.auth.LoginActivity
import com.example.csi_dmce.auth.RegistrationActivity
import com.example.csi_dmce.calendar.CSICalendar
import com.example.csi_dmce.events.event_page
import com.example.csi_dmce.profile.Profile


class Dashboard: AppCompatActivity() {
    private lateinit var btn_registration: Button
    private lateinit var btn_login: Button
    private lateinit var btn_profile: Button
    private lateinit var btn_events: Button
    private lateinit var btn_calendar: Button
    private lateinit var btn_logout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        btn_profile = findViewById(R.id.btn_dashboard_profile)
        btn_profile.setOnClickListener {
            val eventIntent = Intent(this, Profile::class.java)
            startActivity(eventIntent)
        }

        btn_events = findViewById(R.id.btn_dashboard_events)
        btn_events.setOnClickListener {
            val eventIntent = Intent(this, event_page::class.java)
            startActivity(eventIntent)
        }

        btn_calendar = findViewById(R.id.btn_dashboard_calendar)
        btn_calendar.setOnClickListener {
            val eventIntent = Intent(this, CSICalendar::class.java)
            startActivity(eventIntent)
        }

        btn_logout = findViewById(R.id.btn_dashboard_logout)
        btn_logout.setOnClickListener {
            CsiAuthWrapper.deleteAuthToken(applicationContext)
            val intent = Intent(applicationContext, RegistrationActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }
    }
}