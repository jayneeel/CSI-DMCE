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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class RegistrationActivity: AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etStudentId: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvAccountExists: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        etEmail = findViewById(R.id.edit_text_register_email)
        etPassword = findViewById(R.id.edit_text_register_password)
        etStudentId = findViewById(R.id.edit_text_register_student_id)

        btnRegister = findViewById(R.id.button_register)
        btnRegister.setOnClickListener {
            runBlocking {
                if (StudentWrapper.getStudentByEmail(etEmail.text.toString()) != null) {
                    Toast.makeText(applicationContext, "Account already exists", Toast.LENGTH_SHORT).show()
                    return@runBlocking
                } else {
                    val newStudentAuth =  StudentAuth(
                        email = etEmail.text.toString(),
                        password_hash = Helpers.getSha256Hash(etPassword.text.toString()),
                        email_verification = null,
                        forgot_password = null,
                    )

                    val newStudent = Student(
                        student_id = etStudentId.text.toString(),
                        academic_year = Helpers.getAcademicYear(etStudentId.text.toString().slice(0..3).toInt()),
                        department = etStudentId.text.toString().slice(6..7),
                        email = etEmail.text.toString(),
                        email_id_verified = false
                    )

                    runBlocking {
                        StudentAuthWrapper.addStudentAuth(newStudentAuth)
                        StudentWrapper.addStudent(newStudent)
                    }

                    val sharedPref: SharedPreferences = getSharedPreferences(
                        "csi_shared_prefs", Context.MODE_PRIVATE)

                    if (sharedPref.getBoolean("firstTime", true)) {
                        sharedPref.edit().putBoolean("firstTime", false).apply()
                    }

                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    finishAffinity()
                    startActivity(intent)
                }
            }

            runBlocking {
                val otp = Helpers.generateOTP()

                launch {
                    withContext(Dispatchers.IO) {
                        EmailService.sendEmail(
                            otp,
                            EmailKind.EMAIL_VERIFICATION,
                            etEmail.text.toString(),
                            applicationContext
                        )
                    }
                }
                StudentAuthWrapper.createEmailVerificationHashMap(etEmail.text.toString(), otp)
            }

            val intent = Intent(applicationContext, OTPVerificationActivity::class.java)
            intent.putExtra("email_id", etEmail.text.toString())
            intent.putExtra("verification_kind", "email_verification")
            startActivity(intent)
        }

        tvAccountExists = findViewById(R.id.text_view_register_to_login)
        tvAccountExists.setOnClickListener{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }

    }
}