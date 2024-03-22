package com.example.csi_dmce.notifications

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.csi_dmce.R
import com.example.csi_dmce.dashboard.DashboardFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Announcments : AppCompatActivity() {
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
        setContentView(R.layout.activity_announcments)

        ettitle=findViewById(R.id.notification_title_edit)
        etdesc=findViewById(R.id.notification_description_edit)
        eturl=findViewById(R.id.notification_image_edit)
        etannounce=findViewById(R.id.announments_edit)
        etspinner=findViewById(R.id.autoCompleteTextView2)

        tititle=findViewById(R.id.notification_title)
        tidesc=findViewById(R.id.notification_description)
        tiurl=findViewById(R.id.notification_image)
        tiannounce=findViewById(R.id.announcments)
        tispinner=findViewById(R.id.autoviewcon2)

        sannouncemnts=findViewById(R.id.button4)
        sendnotifications2=findViewById(R.id.button3)

        val array = arrayListOf("Admins", "All users")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, array)
        etspinner.setAdapter(adapter)

        sannouncemnts.setOnClickListener {

            val announcemnt=etannounce.text.toString()
            val db = Firebase.firestore
            val map= hashMapOf("1" to announcemnt)
            db.collection("announcment").document("ANNOUNCMENT")
                .set(map).addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added ")
            }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
            MyFirebaseMessagingService.sendFCMMessage(announcemnt,"STAY TUNED","https://firebasestorage.googleapis.com/v0/b/csi-dmce-c6f11.appspot.com/o/announcment%20image%2Fannounce.jpg?alt=media&token=d29cf72d-59cc-457e-a2f1-e602af750639","all")

        }

        sendnotifications2.setOnClickListener {

            var receiver=""
            val title=ettitle.text.toString()
            val desc=etdesc.text.toString()
            val url=eturl.text.toString()
            val spinner=etspinner.text.toString()
            if (spinner=="Admins"){
                receiver= "admins"
            }
            else{
                receiver= "all"
            }


            MyFirebaseMessagingService.sendFCMMessage(title,desc,url,receiver)

        }




    }
}