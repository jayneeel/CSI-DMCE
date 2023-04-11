package com.example.csi_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.csi_admin.user.UserDataActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.events.EventListActivity
import com.example.csi_dmce.ui.WelcomeActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminHomepage : AppCompatActivity() {
    private lateinit var btnLogout: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_homepage)

        btnLogout = findViewById(R.id.button_admin_logout)

        btnLogout.setOnClickListener {
            CsiAuthWrapper.deleteAuthToken(applicationContext)
            val intent = Intent(applicationContext, WelcomeActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }
    }

    fun eventScreen(view: View) {
        val intent = Intent(this, EventListActivity::class.java)
        startActivity(intent)
    }

    fun userDataScreen(view: View) {
        val intent = Intent(this, UserDataActivity::class.java)
        startActivity(intent)
    }
}