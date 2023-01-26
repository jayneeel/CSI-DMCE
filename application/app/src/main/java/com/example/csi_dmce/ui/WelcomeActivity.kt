package com.example.csi_dmce.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.LoginActivity
import com.example.csi_dmce.auth.RegistrationActivity

class WelcomeActivity: AppCompatActivity() {
    private lateinit var btnSignIn: Button
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btnSignIn = findViewById(R.id.button_welcome_sign_in)
        btnSignIn.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
        btnSignUp = findViewById(R.id.button_welcome_sign_up)
        btnSignUp.setOnClickListener {
            val intent = Intent(applicationContext, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}