package com.example.csi_dmce.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.database.StudentAuthWrapper
import com.example.csi_dmce.ui.Dashboard
import com.example.csi_dmce.ui.WelcomeActivity
import com.example.csi_dmce.utils.Helpers
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.runBlocking


class SetPasswordActivity: AppCompatActivity() {
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmNewPassword: EditText
    private lateinit var otpSubmit: Button
    private lateinit var tipass : TextInputLayout
    private lateinit var ticpass : TextInputLayout

    // TODO: Convert this to a fragment.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_password)

        etNewPassword = findViewById(R.id.edit_text_fop_new_password)
        etConfirmNewPassword = findViewById(R.id.edit_text_fop_confirm_new_password)
        otpSubmit = findViewById(R.id.button_forgot_pass_submit)

        tipass=findViewById(R.id.textInputLayout5)
        ticpass=findViewById(R.id.textInputLayout3)

        pass()
        cpass()

        val emailId: String = intent.getStringExtra("email_id").toString()

        otpSubmit.setOnClickListener {
            if (validate()){
            if (etNewPassword.text.toString() == etConfirmNewPassword.text.toString()) {
                val newPassword = Helpers.getSha256Hash(etNewPassword.text.toString())
                runBlocking {
                    StudentAuthWrapper.setPasswordWrapper(emailId, newPassword)
                }

                Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show()

                val dIntent = Intent(this, WelcomeActivity::class.java)
                finishAffinity()
                startActivity(dIntent)

            } else {
                Toast.makeText(applicationContext,"Passwords don't match!", Toast.LENGTH_SHORT).show()
            }
        }}
    }

    private fun cpass() {
        etConfirmNewPassword.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                ticpass.helperText=validatecpass()
            }
        }
    }

    private fun validatecpass(): String? {
        if (etConfirmNewPassword.text.toString().isEmpty()){
            return "ENTER CONFIRM PASSWORD"
        }
        else return null
    }

    private fun pass() {
        etNewPassword.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tipass.helperText=validatepass()
            }
        }
    }

    private fun validatepass(): String? {
        if (etNewPassword.text.toString().isEmpty()) {
            return "ENTER NEW PASSWORD"
        } else if (etNewPassword.text.toString().length < 8) {
            return "PASSWORD TOO SHORT"
        } else if (!etNewPassword.text.toString().matches(".*[A-Z].*".toRegex())) {
            return "MUST CONTAIN 1 UPPER-CASE CHARACTER"
        } else if (!etNewPassword.text.toString().matches(".*[a-z].*".toRegex())) {
            return "MUST CONTAIN 1 LOWER-CASE CHARACTER"
        } else if (!etNewPassword.text.toString().matches(".*[@#/&_].*".toRegex())) {
            return "MUST CONTAIN 1 SPECIAL CHARACTER (@#/&_)"
        } else if (!etNewPassword.text.toString().matches(".*[0-9].*".toRegex())) {
            return "MUST CONTAIN 1 NUMBER CHARACTER"
        } else return null

    }

    private fun validate(): Boolean {
        ticpass.helperText=validatecpass()
        tipass.helperText=validatepass()

        val vpass=  tipass.helperText==null
        val vemail=  ticpass.helperText==null

        return vpass && vemail
    }
}
