package com.example.csi_dmce.auth

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Student
import com.example.csi_dmce.database.StudentAuthWrapper
import com.example.csi_dmce.database.StudentWrapper
import com.otpview.Utils
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.util.Date
import com.google.firebase.Timestamp

class EmailVerification:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.email_otp)
        lateinit var otpSubmit: Button
        otpSubmit=findViewById(R.id.otpSubmit)

        fun generateOTP(): String {
            val genotp = (Math.random() * 9000).toInt() + 1000
            return genotp.toString()
        }

        val emailv_otp = generateOTP() //save in database for temporary time
        val entered_otp:String = intent.extras?.getString("entered_otp").toString() //check if entered before expiry time
        val login_email:String = intent.getStringExtra("login_email").toString()

        runBlocking {  StudentAuthWrapper.SetEmailVerificationStatus(login_email, "unverified") }

        otpSubmit.setOnClickListener {
            runBlocking {
                val ev_map= StudentAuthWrapper.EmailVerificationWrapper(login_email)
                val c_timestamp=ev_map.get("creation_timestamp").toString().toInt()
                val e_timestamp=ev_map.get("expiry_timestamp").toString().toInt()
                StudentAuthWrapper.EmailVerificationWrapper(login_email)
                if(emailv_otp==entered_otp && c_timestamp < e_timestamp){
                    StudentAuthWrapper.SetEmailVerificationStatus( login_email, "verified" ) //updating auth

                    //updating user
                    val studentObj= StudentWrapper.getStudentByEmail(login_email)
                    val student_id= studentObj?.student_id
                    StudentWrapper.setStudentEmailIdVerificationStatus(student_id!!, true)
                }
                else{
                    StudentAuthWrapper.SetEmailVerificationStatus(login_email, "pending")
                }
            }

        }

    }
}



