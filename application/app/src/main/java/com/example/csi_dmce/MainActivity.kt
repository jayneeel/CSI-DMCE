package com.example.csi_dmce

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.ui.Dashboard
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.auth.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = if (CsiAuthWrapper.isAuthenticated(applicationContext)) {
            Intent(this, Dashboard::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
