package com.example.csi_dmce.profile

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.database.Student
import com.example.csi_dmce.database.StudentWrapper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.runBlocking

class Profile: AppCompatActivity() {
    private val REQUEST_CODE_IMAGE_UPSERT = 103
    private var imageUri: Uri? = null

    private var isEditing = false

    private lateinit var studentObject: Student

    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileStudentId: TextView
    private lateinit var etProfileName: EditText
    private lateinit var etStudentId: EditText
    private lateinit var etStudentClass: EditText
    private lateinit var etStudentEmail: EditText
    private lateinit var etStudentContact: EditText
    private lateinit var ivStudentAvatar: ImageView

    private lateinit var ilStudentName: TextInputLayout
    private lateinit var ilStudentId: TextInputLayout
    private lateinit var ilStudentClass: TextInputLayout
    private lateinit var ilStudentEmail: TextInputLayout
    private lateinit var ilStudentContact: TextInputLayout

    private lateinit var btnUpdate: Button
    private lateinit var fabAvatarUpdate: FloatingActionButton

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_profile)

        studentObject = runBlocking { StudentWrapper.getStudent(CsiAuthWrapper.getStudentId(applicationContext)) }!!

        ilStudentClass = findViewById(R.id.input_layout_profile_class)
        ilStudentContact = findViewById(R.id.input_layout_profile_contact)
        ilStudentEmail = findViewById(R.id.input_layout_profile_email)
        ilStudentId = findViewById(R.id.input_layout_profile_studentid)
        ilStudentName = findViewById(R.id.input_layout_profile_name)

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

        etStudentEmail = findViewById(R.id.edit_text_profile_email)
        etStudentEmail.setText(studentObject?.email)

        etStudentContact = findViewById(R.id.edit_text_profile_mobile)
        etStudentContact.setText(studentObject?.phone_number.toString())

        ivStudentAvatar = findViewById(R.id.image_view_user_avatar)
        var avatarExists: Boolean = false

        runBlocking {
            StudentWrapper.getStudentAvatarUrl(studentObject?.student_id!!, studentObject.avatar_extension!!) {
                Glide.with(applicationContext)
                    .setDefaultRequestOptions(RequestOptions())
                    .load(it?: R.drawable.ic_baseline_person_24)
                    .into(ivStudentAvatar)

                avatarExists = it != null
            }
        }

        // When clicked on the avatar, it should show a upscaled version of it, if it exists.
        // Otherwise, open a prompt to pick a new image.
        ivStudentAvatar.setOnClickListener{
            if (!avatarExists) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, REQUEST_CODE_IMAGE_UPSERT)
            } else {
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.component_image_scale_popup)
                val ivFullScale = dialog.findViewById<ImageView>(R.id.image_view_fullscale)
                ivFullScale.setImageDrawable(ivStudentAvatar.drawable)

                dialog.show()
                dialog.window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        fabAvatarUpdate = findViewById(R.id.fab_profile_avatar_update)
        fabAvatarUpdate.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, REQUEST_CODE_IMAGE_UPSERT)
        }

        btnUpdate = findViewById(R.id.button_profile_update)
        btnUpdate.setOnClickListener {
            etProfileName.clearFocus()
            etStudentClass.clearFocus()
            etStudentId.clearFocus()
            etStudentEmail.clearFocus()
            etStudentContact.clearFocus()

            fabAvatarUpdate.isVisible = !fabAvatarUpdate.isVisible

            etProfileName.isFocusableInTouchMode = !etProfileName.isFocusableInTouchMode
            etProfileName.isClickable = !etProfileName.isClickable
            etProfileName.isCursorVisible = !etProfileName.isCursorVisible

            etStudentClass.isFocusableInTouchMode = !etStudentClass.isFocusableInTouchMode
            etStudentClass.isClickable = !etStudentClass.isClickable
            etStudentClass.isCursorVisible = !etStudentClass.isCursorVisible

            etStudentId.isFocusableInTouchMode = !etStudentId.isFocusableInTouchMode
            etStudentId.isClickable = !etStudentId.isClickable
            etStudentId.isCursorVisible = !etStudentId.isCursorVisible

            etStudentEmail.isFocusableInTouchMode = !etStudentEmail.isFocusableInTouchMode
            etStudentEmail.isClickable = !etStudentEmail.isClickable
            etStudentEmail.isCursorVisible = !etStudentEmail.isCursorVisible

            etStudentContact.isFocusableInTouchMode = !etStudentContact.isFocusableInTouchMode
            etStudentContact.isClickable = !etStudentContact.isClickable
            etStudentContact.isCursorVisible = !etStudentContact.isCursorVisible

            ilStudentClass.boxStrokeWidth = if (etStudentContact.isClickable) { 2 } else { 0 }
            ilStudentContact.boxStrokeWidth = if (etStudentContact.isClickable) { 2 } else { 0 }
            ilStudentEmail.boxStrokeWidth = if (etStudentContact.isClickable) { 2 } else { 0 }
            ilStudentId.boxStrokeWidth = if (etStudentContact.isClickable) { 2 } else { 0 }
            ilStudentName.boxStrokeWidth = if (etStudentContact.isClickable) { 2 } else { 0 }

            btnUpdate.text = if (btnUpdate.text == "Confirm update") {
                "Update"
            } else {
                "Confirm update"
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_UPSERT && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            ivStudentAvatar.setImageURI(imageUri)

            runBlocking { StudentWrapper.putStudentAvatar(studentObject, imageUri!!) }
        }
    }
}