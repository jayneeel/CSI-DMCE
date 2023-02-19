package com.example.csi_dmce.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.database.StudentAuthWrapper
import kotlinx.coroutines.runBlocking


// TODO: Implement this.
class ForgotPasswordActivity: AppCompatActivity() {
    private lateinit var etOldPassword: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmNewPassword: EditText
    private lateinit var otpSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.email_otp)
        lateinit var otpSubmit: Button
        otpSubmit=findViewById(R.id.otpSubmit)

        fun generateOTP(): String {
            val genotp = (Math.random() * 9000).toInt() + 1000
            return genotp.toString()
        }

        val fop_otp= generateOTP()
        var entered_otp:String = intent.getStringExtra("entered_otp").toString() //check if entered before expiry time
        val login_email:String = intent.getStringExtra("login_email").toString()

        otpSubmit.setOnClickListener {
            runBlocking {
                val ev_map= StudentAuthWrapper.ForgotPasswordWrapper(login_email)
                val c_timestamp=ev_map.get("creation_timestamp").toString().toInt()
                val e_timestamp=ev_map.get("expiry_timestamp").toString().toInt()
                if(entered_otp==fop_otp && c_timestamp < e_timestamp){
                    val myIntent= Intent(this@ForgotPasswordActivity, setNewPassword::class.java)
                    startActivity(myIntent)
                }
                else  {
                    Toast.makeText(applicationContext, "Incorrect OTP!", Toast.LENGTH_SHORT).show()
                }


            }
        }

    }
}