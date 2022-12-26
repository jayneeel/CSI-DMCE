package com.example.csi_dmce

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room

import com.example.csi_dmce.Utilities

class RegistrationActivity: AppCompatActivity() {
    private lateinit var user_name_box: EditText
    private lateinit var user_email_box: EditText
    private lateinit var user_password_box: EditText
    private lateinit var user_re_password_box: EditText

    private lateinit var register_button: Button
    private lateinit var account_exists:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)

        // Set components
        user_name_box = findViewById(R.id.user_name)
        user_email_box = findViewById(R.id.user_email)
        user_password_box = findViewById(R.id.user_password)
        user_re_password_box = findViewById(R.id.confirm_password)
        register_button = findViewById(R.id.register_button)
        account_exists=findViewById(R.id.account_exists_text)

        // TODO: Move this in a better place so that we can maintain a singleton pattern.
        // Get DB Instance
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "csi-dmce")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
        var user_dao = db.userDao()

        register_button.setOnClickListener {
            var user_name: String = user_name_box.text.toString()
            var user_email: String = user_email_box.text.toString()
            var user_password: String = user_password_box.text.toString()
            var re_user_password: String = user_re_password_box.text.toString()

            // We'll use MD5 for now.
            var utils = Utilities()
            var passwd_hash: String = utils.get_md5_hash(user_password)

            // If the confirmed password and the password match, then we can finally put the
            // user entry in the database.
            if (user_password == re_user_password) {
                var user: User = User(0, user_email, passwd_hash)

                // TODO: Add some assertion to make sure we don't get a null reply.
                user_dao.insert(user)
                Toast.makeText(applicationContext, "Registered sucessfully!", Toast.LENGTH_SHORT).show()
            }
        }

        account_exists.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            Toast.makeText(applicationContext, "Login to continue.", Toast.LENGTH_SHORT).show()
        }


    }
}