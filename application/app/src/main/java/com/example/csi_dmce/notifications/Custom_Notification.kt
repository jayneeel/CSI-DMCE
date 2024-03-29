package com.example.csi_dmce.notifications

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.dashboard.DashMainActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Custom_Notification : AppCompatActivity() {
    private lateinit var ettitle : TextInputEditText
    private lateinit var tititle: TextInputLayout

    private lateinit var etdesc : TextInputEditText
    private lateinit var tidesc: TextInputLayout

    private lateinit var eturl : TextInputEditText
    private lateinit var tiurl: TextInputLayout

    private lateinit var etannounce : TextInputEditText
    private lateinit var tiannounce: TextInputLayout

    private lateinit var etspinner: AutoCompleteTextView
    private lateinit var tispinner: TextInputLayout

    private lateinit var sannouncemnts: Button
    private lateinit var sendnotifications2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_notifications)

        ettitle = findViewById(R.id.notification_title_edit)
        etdesc = findViewById(R.id.notification_description_edit)
        eturl = findViewById(R.id.notification_image_edit)
        etannounce = findViewById(R.id.announments_edit)
        etspinner = findViewById(R.id.autoCompleteTextView2)

        tititle = findViewById(R.id.notification_title)
        tidesc = findViewById(R.id.notification_description)
        tiurl = findViewById(R.id.notification_image)
        tiannounce = findViewById(R.id.announcments)
        tispinner = findViewById(R.id.autoviewcon2)

        sannouncemnts = findViewById(R.id.button4)
        sendnotifications2 = findViewById(R.id.button3)

        val array = arrayListOf("Admins", "All users")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, array)
        etspinner.setAdapter(adapter)

        announce()
        titlefun()
        desc()
        url()
        spinner()


        sannouncemnts.setOnClickListener {
            if (aval()) {
            val announcemnt = etannounce.text.toString()
            val db = Firebase.firestore
            val map = hashMapOf("1" to announcemnt)
            db.collection("announcment").document("ANNOUNCMENT")
                .set(map).addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added ")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
            MyFirebaseMessagingService.sendFCMMessage(
                announcemnt,
                "STAY TUNED",
                "https://firebasestorage.googleapis.com/v0/b/csi-dmce-c6f11.appspot.com/o/announcment%20image%2Fannounce.jpg?alt=media&token=d29cf72d-59cc-457e-a2f1-e602af750639",
                "all"
            )

                Toast.makeText(this, "Announcment sent successfully!!", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, DashMainActivity::class.java)
                startActivity(intent)

            }
        }

        sendnotifications2.setOnClickListener {
            if (nval()) {
            var receiver = ""
            val title = ettitle.text.toString()
            val desc = etdesc.text.toString()
            val url = eturl.text.toString()
            val spinner = etspinner.text.toString()
            if (spinner == "Admins") {
                receiver = "admins"
            } else {
                receiver = "all"
            }
            MyFirebaseMessagingService.sendFCMMessage(title, desc, url, receiver)
                Toast.makeText(this, "Notification sent successfully!!", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, DashMainActivity::class.java)
                startActivity(intent)
        }
    }
    }

    private fun spinner() {
        etspinner.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tispinner.helperText=validatespinner()
            }
        }
    }

    private fun validatespinner(): String? {
        if (etspinner.text.toString().isEmpty()){
            return "Select notification receiver"
        }
        else return null
    }

    private fun titlefun() {
        ettitle.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tititle.helperText=validatetitle()
            }
        }
    }


    private fun validatetitle(): String? {
        if (ettitle.text.toString().isEmpty()){
            return "Enter Announcment"
        }
        else return null
    }

    private fun url() {
        eturl.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tiurl.helperText=validateurl()
            }
        }
    }

    private fun validateurl(): String? {
        if (eturl.text.toString().isEmpty()){
            return "Enter ImageURL with HTTPS OR enter a dash"
        }
        else return null
    }

    private fun desc() {
        etdesc.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tidesc.helperText=validatedesc()
            }
        }
    }

    private fun validatedesc(): String? {
        if (etdesc.text.toString().isEmpty()){
            return "Enter Description"
        }
        else return null
    }

    private fun announce() {
        etannounce.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tiannounce.helperText=validateannounce()
            }
        }
    }

    private fun validateannounce(): String? {
        if (etannounce.text.toString().isEmpty()){
            return "Enter Announcment"
        }
        else return null
    }


    private fun aval(): Boolean {
        tiannounce.helperText=validateannounce()
        return  tiannounce.helperText==null
    }


    private fun nval(): Boolean {
        tititle.helperText=validatetitle()
        tispinner.helperText=validatespinner()
        tiurl.helperText=validateurl()
        tidesc.helperText=validatedesc()

        val a= tititle.helperText==null
        val b=tispinner.helperText==null
        val c=  tiurl.helperText==null
        val d= tidesc.helperText==null

    return a&&b&&c&&d
    }
}