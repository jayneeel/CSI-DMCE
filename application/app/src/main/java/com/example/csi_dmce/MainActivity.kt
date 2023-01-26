package com.example.csi_dmce

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.ui.Dashboard
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.auth.LoginActivity
import com.example.csi_dmce.auth.RegistrationActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = if (CsiAuthWrapper.isAuthenticated(applicationContext)) {
            Intent(this, Dashboard::class.java)
        } else {
            // If the app is running for the first time, then show registration, otherwise show the
            // login screen.
            val sharedPrefs = getSharedPreferences("csi_shared_prefs", Context.MODE_PRIVATE)
            sharedPrefs.getBoolean("firstTime", true).let {
                if (it) {
                    Intent(this, RegistrationActivity::class.java)
                } else {
                    Intent(this, LoginActivity::class.java)
                }
            }
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
