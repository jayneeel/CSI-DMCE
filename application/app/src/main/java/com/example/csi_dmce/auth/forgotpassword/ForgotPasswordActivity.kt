package com.example.csi_dmce.auth.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.OTPVerificationActivity
import com.example.csi_dmce.auth.SetPasswordActivity

class ForgotPasswordActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_forgot_password, ForgotPasswordVerifyEmailFragment())
            .commit()

        val emailVerificationViewModel = ViewModelProvider(this).get(EmailVerificationViewModel::class.java)
        emailVerificationViewModel.emailIsVerified.observe(this, Observer { emailIdExists ->
            if (emailIdExists) {
                val intent = Intent(applicationContext, OTPVerificationActivity::class.java)
                intent.putExtra("email_id", emailVerificationViewModel.emailId.value)
                intent.putExtra("verification_kind", "password_reset_verification")
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Your Email ID isn't correct.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}