package com.example.csi_dmce.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.csi_dmce.R
import com.example.csi_dmce.database.Student
import com.example.csi_dmce.database.StudentAuth
import com.example.csi_dmce.database.StudentAuthWrapper
import com.example.csi_dmce.database.StudentWrapper
import com.example.csi_dmce.utils.Helpers
import kotlinx.coroutines.runBlocking

class RegistrationActivity: AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvAccountExists: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        etName = findViewById(R.id.edit_text_register_name)
        etEmail = findViewById(R.id.edit_text_register_email)
        etPassword = findViewById(R.id.edit_text_register_password)
        etConfirmPassword = findViewById(R.id.edit_text_register_confirm_password)

        val passwordsMatch: Boolean = etPassword.text
            .toString() == etConfirmPassword.text.toString()

        btnRegister = findViewById(R.id.button_register)
        btnRegister.setOnClickListener {
            if (passwordsMatch) {
                val newStudentAuth =  StudentAuth(
                    email = etEmail.text.toString(),
                    password_hash = Helpers.getSha256Hash(etPassword.text.toString())
                )
                runBlocking {
                    StudentAuthWrapper.addStudentAuth(newStudentAuth)
                }

                val sharedPref: SharedPreferences = getSharedPreferences(
                    "csi_shared_prefs", Context.MODE_PRIVATE)

                if (sharedPref.getBoolean("firstTime", true)) {
                    sharedPref.edit().putBoolean("firstTime", false).apply()
                }

                val intent = Intent(applicationContext, LoginActivity::class.java)
                finishAffinity()
                startActivity(intent)

            } else {
                Toast.makeText(applicationContext, "Passwords don't match.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        tvAccountExists = findViewById(R.id.text_view_account_exists)
        tvAccountExists.setOnClickListener{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }
    }
}