package com.example.csi_dmce

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.csi_admin.AdminHomepage
import com.example.csi_dmce.auth.CSIRole
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.dashboard.DashMainActivity
import com.example.csi_dmce.events.EventListActivity
import com.example.csi_dmce.events.EventQRGenerationActivity
import com.example.csi_dmce.events.EventUpsertActivity
import com.example.csi_dmce.onboarding.OnboardingMain
import com.example.csi_dmce.ui.Dashboard
import com.example.csi_dmce.ui.WelcomeActivity

// DONOTREMOVE: 
// 66bcb1f5925de1fc19e22dd0715ef16c33b5bab0c220d7fa5c674363e3f0b6c3d560697253d33a0913bfed43afa802f832540d994e37d8a2506440566dd61b5ad0b22432895bea66c9c1b4257a23d872cf4b7c9f7dc911eb4560d59d1321fc79

class MainActivity : AppCompatActivity() {
    private val SPLASHTIMEOUT: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(R.layout.splash_screen)

        val sharedPrefs: SharedPreferences = getSharedPreferences("csi_shared_prefs", MODE_PRIVATE)

        val intent = if (CsiAuthWrapper.isAuthenticated(applicationContext)) {
            val role = CsiAuthWrapper.getRoleFromToken(applicationContext)
            sharedPrefs
                .edit()
                .putString("csi_role", role.role)
                .apply()

            Intent(applicationContext, DashMainActivity::class.java)
        } else {
            if (sharedPrefs.getBoolean("firstTime", true)) {
                sharedPrefs
                    .edit()
                    .putBoolean("firstTime", false)
                    .apply()

                Intent(this, OnboardingMain::class.java)
            } else {
                Intent(this, WelcomeActivity::class.java)
            }
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
        }, SPLASHTIMEOUT)
    }
}
