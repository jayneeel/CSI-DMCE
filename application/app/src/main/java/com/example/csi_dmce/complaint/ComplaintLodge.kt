package com.example.csi_admin.complaint

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.csi_admin.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ComplaintLodge : AppCompatActivity() {

    private val db : FirebaseFirestore by lazy { Firebase.firestore }
    lateinit var subjectET : TextInputEditText
    lateinit var descriptionET : TextInputEditText
    lateinit var lodgeBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_lodge)
        subjectET = findViewById(R.id.complaint_subject)
        descriptionET = findViewById(R.id.complaint_content)
    }

    fun saveData(view: View) {
        val subjectTxt = subjectET.text.toString();
        val descriptionTxt = descriptionET.text.toString()
        val map = mutableMapOf<String,String>()
        map.put("subject",subjectTxt)
        map.put("description",descriptionTxt)
        db.collection("complaints").add(map)
            .addOnSuccessListener {
                Toast.makeText(this,"Complaint Lodged Successfully",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ComplaintsActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener{
                Toast.makeText(this,"Error $it",Toast.LENGTH_SHORT).show()
            }
    }
}
