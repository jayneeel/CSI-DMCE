package com.example.csi_dmce.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.otpview.OTPListener
import com.otpview.OTPTextView

// TODO: Convert this to a fragment.
class OTPActivity: AppCompatActivity() {
    private lateinit var otpTextView : OTPTextView
    private lateinit var otpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.email_otp_activity)

        otpButton = findViewById(R.id.button_submit_otp)
        otpButton.isEnabled = false

        otpTextView = findViewById(R.id.otp_view)
        otpTextView.requestFocusOTP()
        otpTextView.otpListener = object : OTPListener {
            override fun onInteractionListener() { }

            override fun onOTPComplete(otp: String) {
                otpButton.isEnabled = true
                val intent = Intent(applicationContext, EmailVerificationActivity::class.java)
                intent.putExtra("entered_otp", otp)

                val fintent= Intent(applicationContext, ForgotPasswordActivity::class.java)
                fintent.putExtra("entered_otp", otp )
            }
        }
    }
}