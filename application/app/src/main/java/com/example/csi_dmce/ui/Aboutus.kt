package com.example.csi_dmce.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R


class Aboutus : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutus)





    }

    fun openInstagram(view: View) {
        val uri = Uri.parse("https://www.instagram.com/csidmce/")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    fun openLinkedIn(view: View) {
        val uri = Uri.parse("https://www.linkedin.com/company/csi-dmce/")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    fun openwebsite(view: View) {
        val uri = Uri.parse("https://www.dmce.ac.in/studentclubs/clubs.php?refreshRate=1&id=c4ca4238a0b923820dcc509a6f75849b")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    fun sendmail(view: View){
        val recipient = "csicattdmce@gmail.com"
        val intent=Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        startActivity(Intent.createChooser(intent, "Send Email"))

    }
}