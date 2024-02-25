package com.example.csi_dmce.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_admin.AdminHomepage
import com.example.csi_dmce.MainActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.forgotpassword.ForgotPasswordActivity
import com.example.csi_dmce.dashboard.DashMainActivity
import com.example.csi_dmce.database.StudentAuthWrapper
import com.example.csi_dmce.ui.Dashboard
import com.example.csi_dmce.utils.Helpers
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.runBlocking
import javax.xml.validation.Validator


class LoginActivity: AppCompatActivity() {
    private lateinit var etLoginEmail     : EditText
    private lateinit var etLoginPassword  : EditText
    private lateinit var tvForgotPassword : TextView
    private lateinit var btnLogin          : Button
    private lateinit var tvNoAccount: TextView
    private lateinit var tiemail: TextInputLayout
    private lateinit var tipass: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etLoginEmail      = findViewById(R.id.edit_text_login_email)
        etLoginPassword   = findViewById(R.id.edit_text_login_password)
        tiemail=findViewById(R.id.textInputLayout)
        tipass=findViewById(R.id.textInputLayout2)

        //validate
        email()
        pass()

        btnLogin = findViewById(R.id.button_login)
        btnLogin.setOnClickListener {
            if(Validate()){
            val email: String = etLoginEmail.text.toString()
            val passwordHash: String = Helpers.getSha256Hash(etLoginPassword.text.toString())

            runBlocking {
                StudentAuthWrapper.checkStudentCredentials(email, passwordHash) {
                    it?.let {
                        runBlocking {
                            if (it.email == "csicattdmce@gmail.com") {
                                CsiAuthWrapper.setAuthToken(student = it, ctx = applicationContext, role=CSIRole.ADMIN)
                            } else {
                                CsiAuthWrapper.setAuthToken(student = it, ctx = applicationContext)
                            }

                            val sharedPref = getSharedPreferences(
                                "csi_shared_prefs", Context.MODE_PRIVATE)
                            if (sharedPref.getBoolean("firstTime", true)) {
                                sharedPref.edit().putBoolean("firstTime", false).apply()
                            }

                            val mIntent: Intent = if (CsiAuthWrapper.getRoleFromToken(applicationContext) == CSIRole.ADMIN) {
                                Intent(applicationContext, DashMainActivity::class.java)
                            } else {
                                Intent(applicationContext, DashMainActivity::class.java)
                            }

                            finishAffinity()
                            startActivity(mIntent)
                        }
                    } ?: run {
                        Toast.makeText(applicationContext, "Invalid credentials!", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
        }

        tvForgotPassword = findViewById(R.id.text_view_forgot_password)
        tvForgotPassword.setOnClickListener{
            val intent = Intent(applicationContext, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pass() {
        etLoginPassword.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tipass.helperText=validatepass()
            }
        }
    }

    private fun validatepass(): String? {
        if (etLoginPassword.text.toString().isEmpty()){
            return "ENTER PASSWORD"
        }
        else return null

    }

    private fun email() {
        etLoginEmail.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tiemail.helperText=validateemail()
            }
        }
    }

    private fun validateemail(): String? {
        if (etLoginEmail.text.toString().isEmpty()){
            return "ENTER EMAIL"
        }
        else return null
    }

    private fun Validate(): Boolean {
        tiemail.helperText=validateemail()
        tipass.helperText=validatepass()

        val vpass=  tipass.helperText==null
        val vemail=  tiemail.helperText==null

        return vpass && vemail
    }
}

