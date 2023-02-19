package com.example.csi_dmce.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.database.StudentAuthWrapper
import com.example.csi_dmce.utils.Helpers
import kotlinx.coroutines.runBlocking
import java.util.*


// TODO: Implement this.
class ForgotPasswordActivity: AppCompatActivity() {
    private lateinit var etOldPassword: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmNewPassword: EditText
    private lateinit var submitOtp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.email_otp_activity)

        submitOtp = findViewById(R.id.button_submit_otp)

        //check if entered before expiry time
        var enteredOtp:String = intent.getStringExtra("entered_otp").toString()
        val loginEmail:String = intent.getStringExtra("login_email").toString()

        submitOtp.setOnClickListener {
            runBlocking {
                val emailVerificationMap = StudentAuthWrapper.forgotPasswordWrapper(loginEmail)
                val correctOtp = emailVerificationMap["otp"]
                val expiryTimestamp = emailVerificationMap["expiry_timestamp"].toString().toInt()

                val currentTimestamp = Helpers.generateUnixTimestampFromDate(Date())
                if(enteredOtp == correctOtp && expiryTimestamp < currentTimestamp){
                    val myIntent= Intent(applicationContext, SetPasswordActivity::class.java)
                    startActivity(myIntent)
                }
                else  {
                    Toast.makeText(applicationContext, "Incorrect OTP!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}