package com.example.csi_dmce.auth

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.database.StudentAuthWrapper
import com.example.csi_dmce.database.StudentWrapper
import com.example.csi_dmce.utils.Helpers
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.HashMap


class EmailVerificationActivity: AppCompatActivity() {
    private lateinit var submitOtp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.email_otp_activity)

        //check if entered before expiry time
        val enteredOtp: Int = intent.extras?.getString("entered_otp").toString().toInt()

        // Email ID that is being verified.
        val emailAddress: String = intent.getStringExtra("login_email").toString()

        runBlocking {
            StudentAuthWrapper.setEmailVerificationStatus(emailAddress, "unverified")
        }

        submitOtp = findViewById(R.id.button_submit_otp)
        submitOtp.setOnClickListener {
            runBlocking {
                val verificationHashMap: HashMap<String, Any> = StudentAuthWrapper.EmailVerificationWrapper(emailAddress)

                val otp = verificationHashMap["otp"] as Int
                val expiryTimestamp = verificationHashMap["expiry_timestamp"] as Int

                val currentTimestamp = Helpers.generateUnixTimestampFromDate(Date())
                if (otp == enteredOtp) {
                    if (expiryTimestamp < currentTimestamp) {
                        // Updating auth
                        StudentAuthWrapper.setEmailVerificationStatus( emailAddress, "verified")

                        // Updating user
                        val studentObj = StudentWrapper.getStudentByEmail(emailAddress)!!
                        StudentWrapper.setStudentEmailIdVerificationStatus(studentObj.email!!, true)
                    } else {
                        Toast.makeText(applicationContext, "OTP has expired!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "OTP is incorrect!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}



