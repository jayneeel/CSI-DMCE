package com.example.csi_dmce.auth

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

        val Button = findViewById<Button>(R.id.otpSubmit)
        otpTextView = findViewById(R.id.otp_view) as OTPTextView
        otpTextView.requestFocusOTP()
        otpTextView.otpListener = object : OTPListener {
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otp: String) {
                Toast.makeText(this@otp, "The OTP is $otp", Toast.LENGTH_SHORT).show()
                //make unclickable button clickable

            }
        }

        Button.setOnClickListener { otpTextView.showSuccess() }
        //if all fields are filled and match

    }

}