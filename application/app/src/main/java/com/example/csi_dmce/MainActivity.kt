package com.example.csi_dmce

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myIntent = Intent(this, Dashboard::class.java)
        startActivity(myIntent)
    }

}
