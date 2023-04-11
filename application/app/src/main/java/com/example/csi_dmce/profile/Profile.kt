package com.example.csi_dmce.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.database.StudentWrapper
import com.example.csi_dmce.profile.ProfileEvents
import com.example.csi_dmce.profile.ProfileEventsAdapter
import kotlinx.coroutines.runBlocking

class Profile: AppCompatActivity() {
    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileStudentId: TextView
    private lateinit var etProfileName: EditText
    private lateinit var etStudentId: EditText
    private lateinit var etStudentClass: EditText
    private lateinit var etStudentContact: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_profile)

        val studentObject = runBlocking { StudentWrapper.getStudent(CsiAuthWrapper.getStudentId(applicationContext))}

        tvProfileName = findViewById(R.id.text_view_profile_name)
        tvProfileName.setText(studentObject?.name)

        tvProfileStudentId = findViewById(R.id.text_view_profile_student_id)
        tvProfileStudentId.setText(studentObject?.student_id)

        etProfileName = findViewById(R.id.edit_text_profile_name)
        etProfileName.setText(studentObject?.name)

        etStudentId = findViewById(R.id.edit_text_profile_studentid)
        etStudentId.setText(studentObject?.student_id)

        etStudentClass = findViewById(R.id.edit_text_student_class)
        etStudentClass.setText(studentObject?.department + "-" + studentObject?.academic_year + "-" + studentObject?.division)

        etStudentContact = findViewById(R.id.edit_text_profile_mobile)
        etStudentContact.setText(studentObject?.phone_number.toString())
    }
}