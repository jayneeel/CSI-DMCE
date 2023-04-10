package com.example.csi_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    lateinit var username : TextInputEditText
    lateinit var password : TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        username = findViewById(R.id.adminEmail)
        password = findViewById(R.id.adminPassword)
    }

    fun adminHomepageScreen(view: View) {
        val intent = Intent(this,AdminHomepage::class.java)
        startActivity(intent)
    }
}