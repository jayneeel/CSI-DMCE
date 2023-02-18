package com.example.csi_dmce.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.database.StudentAuthWrapper
import com.example.csi_dmce.utils.Helpers
import kotlinx.coroutines.runBlocking


class setNewPassword: AppCompatActivity() {

    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmNewPassword: EditText
    private lateinit var otpSubmit: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etNewPassword= findViewById(R.id.edit_text_fop_new_password)
        etConfirmNewPassword=findViewById(R.id.edit_text_fop_confirm_new_password)
        otpSubmit=findViewById(R.id.button_forgot_pass_submit)

        val login_email:String = intent.getStringExtra("login_email").toString()

        otpSubmit.setOnClickListener {
            if(etNewPassword==etConfirmNewPassword){
                var newPassword = Helpers.getSha256Hash(etNewPassword.text.toString())
                runBlocking {
                    StudentAuthWrapper.SetPasswordWrapper(login_email, newPassword)
                }

            }
        }
    }
}
