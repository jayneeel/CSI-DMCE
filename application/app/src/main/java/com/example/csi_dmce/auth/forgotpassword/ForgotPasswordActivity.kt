package com.example.csi_dmce.auth.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.SetPasswordActivity

class ForgotPasswordActivity: AppCompatActivity() {
    private var emailIdExists: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_forgot_password, ForgotPasswordVerifyEmailFragment())
            .commit()

        val emailVerificationViewModel = ViewModelProvider(this).get(EmailVerificationViewModel::class.java)
        emailVerificationViewModel.emailIsVerified.observe(this, Observer { emailIdExists ->
            if (emailIdExists) {
                val intent = Intent(applicationContext, SetPasswordActivity::class.java)
                startActivity(intent)
            }
            Toast.makeText(applicationContext, "Your Email ID isn't correct.", Toast.LENGTH_SHORT).show()
        })
    }
}