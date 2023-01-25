package com.example.csi_dmce.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.MainActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.database.StudentAuthWrapper
import com.example.csi_dmce.ui.Dashboard
import com.example.csi_dmce.utils.Helpers
import kotlinx.coroutines.runBlocking


class LoginActivity: AppCompatActivity() {
    private lateinit var et_login_email     : EditText
    private lateinit var et_login_password  : EditText
    private lateinit var btn_login          : Button
    private lateinit var tv_forgot_password : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        et_login_email      = findViewById(R.id.edit_text_login_email)
        et_login_password   = findViewById(R.id.edit_text_login_password)
        et_login_email.setText("amit@gmail.com")
        et_login_password.setText("root")

        btn_login = findViewById(R.id.button_login)
        btn_login.setOnClickListener {
            val email: String = et_login_email.text.toString()
            val passwordHash: String = Helpers.getSha256Hash(et_login_password.text.toString())
            runBlocking {
                StudentAuthWrapper.checkStudentCredentials(email, passwordHash) {
                    it?.let {
                        runBlocking {
                            CsiAuthWrapper.setAuthToken(student = it, context = applicationContext)
                            val mIntent = Intent(applicationContext, MainActivity::class.java)
                            finishAffinity()
                            startActivity(mIntent)
                        }
                    } ?: run {
                        Toast.makeText(applicationContext, "Invalid credentials!", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
        tv_forgot_password = findViewById(R.id.text_view_forgot_password)
    }
}

