package com.example.csi_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.csi_admin.user.UserDataActivity

class AdminHomepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_homepage)
    }

    fun eventScreen(view: View) {
        val intent = Intent(this,EventsDataActivity::class.java)
        startActivity(intent)
    }

    fun userDataScreen(view: View) {
        val intent = Intent(this, UserDataActivity::class.java)
        startActivity(intent)
    }
}