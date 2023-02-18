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
                StudentAuthWrapper.EmailVerificationWrapper(login_email)
                if(emailv_otp==entered_otp){ // add timestamp constraint also
                    StudentAuthWrapper.SetEmailVerificationStatus( login_email, "verified" ) //updating auth

                    // check if this the correct way to get the timestamp from the map
                    var ev_map= StudentAuthWrapper.EmailVerificationWrapper(login_email)
                    var c_timestamp=ev_map.get("creation_timestamp")

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