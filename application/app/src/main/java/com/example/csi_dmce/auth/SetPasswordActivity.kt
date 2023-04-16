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
import kotlinx.coroutines.runBlocking


class SetPasswordActivity: AppCompatActivity() {
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmNewPassword: EditText
    private lateinit var otpSubmit: Button

    // TODO: Convert this to a fragment.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_password)

        etNewPassword = findViewById(R.id.edit_text_fop_new_password)
        etConfirmNewPassword = findViewById(R.id.edit_text_fop_confirm_new_password)
        otpSubmit = findViewById(R.id.button_forgot_pass_submit)

        val emailId: String = intent.getStringExtra("email_id").toString()

        otpSubmit.setOnClickListener {
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
        }
    }
}
