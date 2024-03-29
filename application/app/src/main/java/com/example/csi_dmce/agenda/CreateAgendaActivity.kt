package com.example.csi_dmce.agenda

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.csi_dmce.utils.Helpers
import com.example.csi_dmce.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.runBlocking
import java.util.Date

class CreateAgendaActivity : AppCompatActivity() {
    private lateinit var etTitle: TextInputEditText
    private lateinit var etTask: TextInputEditText
    private lateinit var btnAddTask: Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_agenda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etTitle = findViewById(R.id.edit_text_agenda_title)
        etTask = findViewById(R.id.edit_text_agenda_task)
        btnAddTask = findViewById(R.id.button_add_agenda)

        btnAddTask.setOnClickListener{
            runBlocking {
                AgendaWrapper.addAgenda(
                    Agenda(title = etTitle.text.toString(),
                    task = etTask.text.toString(),
                    time = Helpers.generateUnixTimestampFromDate(Date())))
            }
                Toast.makeText(this, "Agenda Created successfully!", Toast.LENGTH_SHORT).show()
                finish()
        }
    }
}