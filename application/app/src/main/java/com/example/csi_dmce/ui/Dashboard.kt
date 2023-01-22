package com.example.csi_dmce.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.Login
import com.example.csi_dmce.auth.RegistrationActivity
import com.example.csi_dmce.calendar.CSICalendar
import com.example.csi_dmce.database.FirebaseDBWrapper.commitDBAction
import com.example.csi_dmce.events.event_page
import com.example.csi_dmce.profile.Profile
import kotlinx.coroutines.runBlocking

class Dashboard: AppCompatActivity() {
    private lateinit var btn_registration: Button
    private lateinit var btn_login: Button
    private lateinit var btn_profile: Button
    private lateinit var btn_events: Button
    private lateinit var btn_calendar: Button

    private lateinit var db_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        db_button = findViewById(R.id.db_btn)
        db_button.setOnClickListener {
            runBlocking {
                commitDBAction()
            }
        }

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
            val eventIntent = Intent(this, event_page::class.java)
            startActivity(eventIntent)
        }

        btn_calendar = findViewById(R.id.btn_dashboard_calendar)
        btn_calendar.setOnClickListener {
            val eventIntent = Intent(this, CSICalendar::class.java)
            startActivity(eventIntent)
        }
    }
}