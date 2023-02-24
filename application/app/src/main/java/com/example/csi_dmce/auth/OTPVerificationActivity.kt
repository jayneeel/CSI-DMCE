package com.example.csi_dmce.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.database.StudentAuthWrapper
import com.example.csi_dmce.database.StudentWrapper
import com.example.csi_dmce.ui.Dashboard
import com.example.csi_dmce.utils.Helpers
import com.otpview.OTPListener
import com.otpview.OTPTextView
import kotlinx.coroutines.runBlocking
import java.util.*

// TODO: Convert this to a fragment.
class OTPVerificationActivity: AppCompatActivity() {
    private lateinit var tvOtp : OTPTextView
    private lateinit var btnOtpSubmit: Button
    private var inputOtp: Int? = null

    // The e-mail address that is being verified.
    private lateinit var emailId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otp_verification_activity)

        emailId = intent.getStringExtra("email_id").toString()
        val verificationKind: String = intent.getStringExtra("verification_kind").toString()
        btnOtpSubmit = findViewById(R.id.button_submit_otp)
        btnOtpSubmit.isEnabled = false

        tvOtp = findViewById(R.id.otp_view)
        tvOtp.requestFocusOTP()
        tvOtp.otpListener = object : OTPListener {
            override fun onInteractionListener() { }

            override fun onOTPComplete(otp: String) {
                btnOtpSubmit.isEnabled = true
                inputOtp = otp.toInt()
            }
        }

        btnOtpSubmit.setOnClickListener {
            val otpIsCorrect: Boolean = when(verificationKind) {
                "email_verification" -> runBlocking { performEmailVerificationTasks() }
                "password_reset_verification" -> runBlocking { performPasswordResetTasks() }
                else -> throw NotImplementedError("OTP verification kind is incorrect.")
            }

            if (!otpIsCorrect) {
                Toast.makeText(applicationContext, "The given OTP is incorrect!", Toast.LENGTH_SHORT).show()
            }

            val intent: Intent = when(verificationKind) {
                "email_verification" -> Intent(applicationContext, Dashboard::class.java)
                "password_reset_verification" -> Intent(applicationContext, SetPasswordActivity::class.java)
                else -> throw NotImplementedError("OTP verification kind is incorrect.")
            }
            startActivity(intent)
        }
    }

    private suspend fun performEmailVerificationTasks(): Boolean {
        val currentTimestamp: Long = Helpers.generateUnixTimestampFromDate(Date())
        val verificationHashMap: HashMap<String, Any> = StudentAuthWrapper.getEmailVerificationHashMap(emailId)

        val correctOtp = verificationHashMap["otp"] as Int
        val expiryTimestamp = verificationHashMap["expiry_timestamp"] as Int

        if (inputOtp != correctOtp) {
            Toast.makeText(applicationContext, "Incorrect OTP!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (currentTimestamp > expiryTimestamp) {
            Toast.makeText(applicationContext, "OTP has expired!", Toast.LENGTH_SHORT).show()
            return false
        }


        // Updating auth
        StudentAuthWrapper.setEmailVerificationStatus(emailId, "verified")

        // Updating user
        val studentObj = StudentWrapper.getStudentByEmail(emailId)!!
        StudentWrapper.setStudentEmailIdVerificationStatus(studentObj.email!!, true)

        return true
    }

    private suspend fun performPasswordResetTasks(): Boolean {
        val emailVerificationMap = StudentAuthWrapper.forgotPasswordWrapper(emailId)
        val correctOtp = emailVerificationMap["otp"]
        val expiryTimestamp = emailVerificationMap["expiry_timestamp"].toString().toInt()

        val currentTimestamp = Helpers.generateUnixTimestampFromDate(Date())

        if (inputOtp != correctOtp) {
            Toast.makeText(applicationContext, "Incorrect OTP!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (currentTimestamp > expiryTimestamp) {
            Toast.makeText(applicationContext, "OTP has expired!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}