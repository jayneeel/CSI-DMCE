package com.example.csi_dmce

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Dashboard: AppCompatActivity() {
    private lateinit var btn_registration: Button
    private lateinit var btn_login: Button
    private lateinit var btn_profile: Button
    private lateinit var btn_events: Button
    private lateinit var btn_calendar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        btn_registration = findViewById(R.id.btn_dashboard_register)
        btn_registration.setOnClickListener {
            val eventIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(eventIntent)
        }

        btn_login = findViewById(R.id.btn_dashboard_login)
        btn_login.setOnClickListener {
            val eventIntent = Intent(this, Login::class.java)
            startActivity(eventIntent)
        }

        btn_profile = findViewById(R.id.btn_dashboard_profile)
        btn_profile.setOnClickListener {
            val eventIntent = Intent(this, Profile::class.java)
            startActivity(eventIntent)
        }

        btn_events = findViewById(R.id.btn_dashboard_events)
        btn_events.setOnClickListener {
            val eventIntent = Intent(this, event_recycler::class.java)
            startActivity(eventIntent)
        }

        btn_calendar = findViewById(R.id.btn_dashboard_calendar)
        btn_calendar.setOnClickListener {
            val eventIntent = Intent(this, CSICalendar::class.java)
            startActivity(eventIntent)
        }
    }
}