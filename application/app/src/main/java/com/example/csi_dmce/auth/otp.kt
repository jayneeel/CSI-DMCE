package com.example.csi_dmce.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.otpview.OTPListener
import com.otpview.OTPTextView

class otp: AppCompatActivity() {
    private lateinit var otpTextView : OTPTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.email_otp)

        val otpButton = findViewById<Button>(R.id.otpSubmit)
        otpButton.isEnabled = false
        otpTextView = findViewById(R.id.otp_view) as OTPTextView
        otpTextView.requestFocusOTP()
        otpTextView.otpListener = object : OTPListener {
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otp: String) {
                otpButton.isEnabled = true
                val intent = Intent(applicationContext, EmailVerification::class.java)
                intent.putExtra("entered_otp", otp)

                val fintent= Intent(applicationContext, ForgotPasswordActivity::class.java)
                fintent.putExtra("entered_otp", otp )

            }
        }


    }

}