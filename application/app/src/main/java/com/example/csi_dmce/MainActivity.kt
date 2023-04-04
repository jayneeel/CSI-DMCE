package com.example.csi_dmce

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.events.EventListActivity
import com.example.csi_dmce.events.EventUpsertActivity
import com.example.csi_dmce.ui.Dashboard
import com.example.csi_dmce.ui.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val SPLASHTIMEOUT: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val intent = Intent(this, EventUpsertActivity::class.java)
        startActivity(intent)

//        setContentView(R.layout.splash_screen)
//
//        val sharedPrefs: SharedPreferences = getSharedPreferences("csi_shared_prefs", MODE_PRIVATE)
//
//        val intent = if (CsiAuthWrapper.isAuthenticated(applicationContext)) {
//            val role = CsiAuthWrapper.getRoleFromToken(applicationContext)
//            sharedPrefs
//                .edit()
//                .putString("csi_role", role.role)
//                .apply()
//
//            Intent(this, Dashboard::class.java)
//        } else {
//            Intent(this, WelcomeActivity::class.java)
//        }
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//        Handler(Looper.getMainLooper()).postDelayed({
//            startActivity(intent)
//        }, SPLASHTIMEOUT)
//    }
    }
}
