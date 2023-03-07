package com.example.csi_dmce

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.ui.Dashboard
import com.example.csi_dmce.ui.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val SPLASHTIMEOUT: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        val intent = if (CsiAuthWrapper.isAuthenticated(applicationContext)) {
            Intent(this, Dashboard::class.java)
        } else {
            Intent(this, WelcomeActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
        }, SPLASHTIMEOUT)
    }
}
