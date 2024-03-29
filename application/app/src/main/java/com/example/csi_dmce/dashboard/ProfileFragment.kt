package com.example.csiappdashboard

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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

class ProfileFragment : Fragment() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_profile, container, false)

        studentObject = runBlocking { StudentWrapper.getStudent(CsiAuthWrapper.getStudentId(view.context)) }!!

        ivStudentAvatar = view.findViewById(R.id.image_view_user_avatar)
        var avatarExists: Boolean = false

        runBlocking {
            StudentWrapper.getStudentAvatarUrl(studentObject?.student_id!!, studentObject.avatar_extension!!) {
                Glide.with(view.context)
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
                val dialog = Dialog(view.context)
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


        ilStudentClass = view.findViewById(R.id.input_layout_profile_class)
        ilStudentContact = view.findViewById(R.id.input_layout_profile_contact)
        ilStudentEmail = view.findViewById(R.id.input_layout_profile_email)
        ilStudentId = view.findViewById(R.id.input_layout_profile_studentid)
        ilStudentName = view.findViewById(R.id.input_layout_profile_name)

        tvProfileName = view.findViewById(R.id.text_view_profile_name)
        tvProfileName.setText(studentObject.name ?: "User")

        tvProfileStudentId = view.findViewById(R.id.text_view_profile_student_id)
        tvProfileStudentId.setText(studentObject.student_id)

        etProfileName = view.findViewById(R.id.edit_text_profile_name)
        etProfileName.setText(studentObject.name ?: "User")

        etStudentId = view.findViewById(R.id.edit_text_profile_studentid)
        etStudentId.setText(studentObject.student_id)

        etStudentClass = view.findViewById(R.id.edit_text_student_class)
        etStudentClass.setText((studentObject.department ?: "DEPT") + "-" + (studentObject.academic_year ?: "YEAR") + "-" + (studentObject.division ?: "A/B"))

        etStudentEmail = view.findViewById(R.id.edit_text_profile_email)
        etStudentEmail.setText(studentObject?.email)

        etStudentContact = view.findViewById(R.id.edit_text_profile_mobile)
        etStudentContact.setText(if (studentObject.phone_number == null) "" else studentObject.phone_number.toString())

        fabAvatarUpdate = view.findViewById(R.id.fab_profile_avatar_update)
        fabAvatarUpdate.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, REQUEST_CODE_IMAGE_UPSERT)
        }

        btnUpdate = view.findViewById(R.id.button_profile_update)
        btnUpdate.setOnClickListener {
            if (isEditing) {
                updateProfile()
            }

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

            isEditing = !isEditing
        }

        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_UPSERT && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            ivStudentAvatar.setImageURI(imageUri)

            runBlocking { StudentWrapper.putStudentAvatar(studentObject, imageUri!!) }
        }
    }

    fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    private fun updateProfile() {
        val updatedStudentObject = studentObject.copy()
        updatedStudentObject.name = etProfileName.text.toString()
        updatedStudentObject.department = etStudentClass.text.split("-")[0]
        updatedStudentObject.academic_year = etStudentClass.text.split("-")[1]
        updatedStudentObject.division = etStudentClass.text.split("-")[2]
        updatedStudentObject.student_id = etStudentId.text.toString()

        if (!etStudentEmail.text.isValidEmail()) {
            Toast.makeText(view?.context, "Invalid Email!", Toast.LENGTH_SHORT).show()
            return
        }

        updatedStudentObject.email = etStudentEmail.text.toString()
        updatedStudentObject.phone_number = if (etStudentContact.text.isNullOrEmpty()) null else etStudentContact.text.toString().toLong()

        if (imageUri != null) {
            // Update profile pic
            updatedStudentObject.avatar_extension =  runBlocking {
                StudentWrapper.putStudentAvatar(updatedStudentObject, imageUri!!)
            }
        }

        runBlocking { StudentWrapper.updateStudent(studentObject, updatedStudentObject) }
    }
}