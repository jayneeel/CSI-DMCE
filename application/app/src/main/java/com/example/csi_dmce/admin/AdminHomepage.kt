package com.example.csi_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.csi_admin.user.UserDataActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.events.EventListActivity

class AdminHomepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_homepage)
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