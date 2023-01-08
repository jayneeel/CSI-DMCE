package com.example.csi_dmce.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.csi_dmce.ui.Dashboard
import com.example.csi_dmce.profile.Profile
import com.example.csi_dmce.R
import com.example.csi_dmce.database.AppDatabase
import com.example.csi_dmce.utils.Helpers

class Login: AppCompatActivity() {
    private lateinit var login_email: EditText
    private lateinit var login_password: EditText
    private lateinit var login_button: Button
    private lateinit var without_account:Button

    private lateinit var tv_forgot_password: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_email = findViewById(R.id.editTextTextEmailAddress)
        login_password = findViewById(R.id.editTextTextPassword)
        login_button=findViewById(R.id.loginb)
        without_account=findViewById(R.id.woacc)
        tv_forgot_password = findViewById(R.id.tv_forgot_password)

        tv_forgot_password.setOnClickListener{
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        login_button.setOnClickListener {
            var log_email: String = login_email.text.toString()
            var log_password: String = login_password.text.toString()
            if(log_email.isEmpty() || log_password.isEmpty()){
                Toast.makeText(applicationContext, "Please fill all the fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                var utils = Helpers()
                var log_password_hash: String = utils.get_md5_hash(log_password)

                //Performing Query
                val db = AppDatabase.getInstance(this)
                var user_dao = db.userDao()
                var UserEntity = user_dao.login_function(log_email, log_password_hash)
                if (!UserEntity) {
                    Toast.makeText(applicationContext, "Invalid credentials.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    Toast.makeText(applicationContext, "Welcome!", Toast.LENGTH_SHORT).show()

                }
            }


        }
        without_account.setOnClickListener{
            val intent = Intent(this, Dashboard::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }
    }


}