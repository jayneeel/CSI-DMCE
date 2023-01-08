package com.example.csi_dmce.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.csi_dmce.R


class edit_profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val name_edit = findViewById<EditText>(R.id.name_edit)
        val branch_edit = findViewById<EditText>(R.id.branch_edit)
        val rollno_edit = findViewById<EditText>(R.id.rollno_edit)
        val email_edit = findViewById<EditText>(R.id.email_edit)
        val password_edit = findViewById<EditText>(R.id.password_edit)
        val edit = findViewById<Button>(R.id.edit)


        edit.setOnClickListener(View.OnClickListener {

            val name = name_edit.text.toString()
            val branch = branch_edit.text.toString()
            val roll = rollno_edit.text.toString()
            val rollno :Int = roll.toInt()
            val email = email_edit.text.toString()
            val password = password_edit.text.toString()

            val checkdetails = profileCheck(name , branch , rollno , email , password)


            val checkname= checkdetails.name
            val checkbranch=checkdetails.branch
            val checkroll =checkdetails.roll
            val checkemail = checkdetails.email
            val checkpassword= checkdetails.password



          startActivity(Intent(this,Profile::class.java)
              .putExtra("name 1801",checkname)
              .putExtra("branch 1801",checkbranch)
              .putExtra("roll 1801",checkroll)
              .putExtra("email 1801",checkemail)
              .putExtra("password 1801",checkpassword)

          )


        })


    }
}