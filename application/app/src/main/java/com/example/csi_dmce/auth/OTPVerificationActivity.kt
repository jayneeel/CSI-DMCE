package com.example.csi_dmce.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
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
    private lateinit var etOtp : OTPTextView
    private lateinit var btnOtpSubmit: Button
    private var inputOtp: Int? = null

    private lateinit var tvVerificationEmail: TextView

    // The e-mail address that is being verified.
    private lateinit var emailId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        Log.d("OTP_VERIF", intent.getStringExtra("verification_kind").toString())

        emailId = intent.getStringExtra("email_id").toString()
        val verificationKind: EmailKind = EmailKind
            .fromKind(intent.getStringExtra("verification_kind").toString())

        tvVerificationEmail = findViewById(R.id.text_view_verification_email)
        tvVerificationEmail.text = emailId

        btnOtpSubmit = findViewById(R.id.button_submit_otp)
        btnOtpSubmit.isClickable = false
        btnOtpSubmit.setBackgroundColor(Color.parseColor("#808080"))

        etOtp = findViewById(R.id.otp_view)
        etOtp.requestFocusOTP()
        etOtp.otpListener = object : OTPListener {
            override fun onInteractionListener() { }

            override fun onOTPComplete(otp: String) {
                btnOtpSubmit.isClickable = true
                // TODO: Fix hardcoded RGB value
                btnOtpSubmit.setBackgroundColor(Color.parseColor("#0A0630"))
                inputOtp = otp.toInt()
            }
        }

        btnOtpSubmit.setOnClickListener {
            val otpIsCorrect: Boolean = when(verificationKind) {
                EmailKind.EMAIL_VERIFICATION -> runBlocking { performEmailVerificationTasks() }
                EmailKind.RESET_PASSWORD_VERIFICATION -> runBlocking { performPasswordResetTasks() }
            }

            if (!otpIsCorrect) {
                Toast.makeText(applicationContext, "The given OTP is incorrect!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent: Intent = when(verificationKind) {
                EmailKind.EMAIL_VERIFICATION -> Intent(applicationContext, Dashboard::class.java)
                EmailKind.RESET_PASSWORD_VERIFICATION -> Intent(applicationContext, SetPasswordActivity::class.java)
            }
            intent.putExtra("email_id", emailId)
            startActivity(intent)
        }
    }

    private suspend fun performEmailVerificationTasks(): Boolean {
        val currentTimestamp: Long = Helpers.generateUnixTimestampFromDate(Date())
        val verificationHashMap: HashMap<String, Any> = StudentAuthWrapper.getEmailVerificationHashMap(emailId)

        val correctOtp: Long = verificationHashMap["otp"] as Long
        val expiryTimestamp: Long = verificationHashMap["expiry_timestamp"] as Long

        if (inputOtp!!.toLong() != correctOtp) {
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
        StudentWrapper.setStudentEmailIdVerificationStatus(studentObj.student_id!!, true)

        // Set auth token
        CsiAuthWrapper.setAuthToken(applicationContext, studentObj)

        return true
    }

    private suspend fun performPasswordResetTasks(): Boolean {
        val forgotPasswordMap = StudentAuthWrapper.forgotPasswordWrapper(emailId)
        val correctOtp = forgotPasswordMap["otp"]
        val expiryTimestamp = forgotPasswordMap["expiry_timestamp"].toString().toInt()

        val currentTimestamp = Helpers.generateUnixTimestampFromDate(Date())

        if (inputOtp.toString() != correctOtp.toString()) {
            Log.d("DB_PWD_RESET", "HIT INCORRECT OTP")
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