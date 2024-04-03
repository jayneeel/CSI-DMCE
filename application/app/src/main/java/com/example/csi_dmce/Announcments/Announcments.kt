package com.example.csi_dmce.Announcments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.csi_dmce.R
import com.example.csi_dmce.dashboard.DashMainActivity
import com.example.csi_dmce.notifications.MyFirebaseMessagingService
import com.example.csi_dmce.utils.Helpers
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Date

class Announcments : AppCompatActivity() {

    private lateinit var sendnotifications2: Button
    private lateinit var ettitle : TextInputEditText
    private lateinit var tititle: TextInputLayout

    private lateinit var etdesc : TextInputEditText
    private lateinit var tidesc: TextInputLayout

    private lateinit var etspinner: AutoCompleteTextView
    private lateinit var tispinner: TextInputLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcments)

        sendnotifications2 = findViewById(R.id.button3)

        ettitle = findViewById(R.id.notification_title_edit)
        etdesc = findViewById(R.id.notification_description_edit)
        etspinner = findViewById(R.id.autoCompleteTextView2)

        tititle = findViewById(R.id.notification_title)
        tidesc = findViewById(R.id.notification_description)
        tispinner = findViewById(R.id.autoviewcon2)

        val array = arrayListOf("Admins", "All users")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, array)
        etspinner.setAdapter(adapter)

        titlefun()
        desc()
        spinner()


        sendnotifications2.setOnClickListener {
            if (nval()) {
                var receiver = ""
                val title = ettitle.text.toString()
                val desc = etdesc.text.toString()
                val spinner = etspinner.text.toString()
                if (spinner == "Admins") {
                    receiver = "admins"
                } else {
                    receiver = "all"
                }

                val time= Helpers.generateUnixTimestampFromDate(Date()).toString()
                val announcemnt = ettitle.text.toString()
                val db = Firebase.firestore
                val map = hashMapOf("title" to title)
                map.put("description",desc)
                map.put("receiver",receiver)
                map.put("time",time)
                db.collection("announcment").document("ANNOUNCMENT"+time)
                    .set(map).addOnSuccessListener {
                        Log.d(ContentValues.TAG, "DocumentSnapshot added ")
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding document", e)
                    }


                MyFirebaseMessagingService.sendFCMMessage(
                    announcemnt,
                    desc,
                    "https://firebasestorage.googleapis.com/v0/b/csi-dmce-c6f11.appspot.com/o/announcment%20image%2Fannounce.jpg?alt=media&token=d29cf72d-59cc-457e-a2f1-e602af750639",
                    receiver
                )

                Toast.makeText(this, "Announcment sent successfully!!", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, DashMainActivity::class.java)
                startActivity(intent)

            }
        }


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
    private fun nval(): Boolean {
        tititle.helperText=validatetitle()
        tispinner.helperText=validatespinner()
        tidesc.helperText=validatedesc()

        val a= tititle.helperText==null
        val b=tispinner.helperText==null
        val d= tidesc.helperText==null

        return a&&b&&d


    }
}