package com.example.csi_admin.complaint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.database.Complaint
import com.example.csi_dmce.database.ComplaintWrapper
import com.example.csi_dmce.utils.Helpers
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import java.util.Date

class ComplaintLodge : AppCompatActivity() {
    private lateinit var etSubject : TextInputEditText
    private lateinit var etDescription : TextInputEditText
    private lateinit var btnLodge : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_lodge)

        etSubject = findViewById(R.id.edit_text_complaint_subject)
        etDescription = findViewById(R.id.edit_text_complaint_description)
        btnLodge = findViewById(R.id.button_complaint_lodge)
        btnLodge.setOnClickListener {
            runBlocking {
                ComplaintWrapper.addComplaint(
                    Complaint(
                        student_id = intent.getStringExtra("student_id"),
                        student_name = intent.getStringExtra("student_name"),
                        avatar_extension = intent.getStringExtra("avatar_extension"),
                        subject = etSubject.text.toString(),
                        description = etDescription.text.toString(),
                        is_resolved = false,
                        registered_at = Helpers.generateUnixTimestampFromDate(Date())
                    )
                )
            }

            Toast.makeText(this, "Complaint lodged successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
