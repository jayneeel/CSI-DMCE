package com.example.csi_dmce.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R

class ForgotPassword: AppCompatActivity() {
    private lateinit var tv_user_phone_number: TextView
    private lateinit var btn_otp_verify: Button
    private lateinit var btn_otp_resend: Button
    private lateinit var et_otp_value: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password)
    }
}