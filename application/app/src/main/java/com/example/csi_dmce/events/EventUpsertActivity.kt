package com.example.csi_dmce.events

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.database.EventWrapper
import com.example.csi_dmce.utils.Helpers
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.*

class EventUpsertActivity: AppCompatActivity() {
    private val REQUEST_CODE_IMAGE_UPSERT: Int = 101
    private var imageUri: Uri? = null

    private lateinit var eventDate: Date

    private lateinit var upsertButton: Button

    private lateinit var etEventTitle: EditText
    private lateinit var etEventDescription: EditText
    private lateinit var etEventDateTime: EditText
    private lateinit var etEventSpeaker: EditText
    private lateinit var etEventVenue: EditText
    private lateinit var etEventPrerequisites: EditText
    private lateinit var ivEventPoster: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_upsert)

        val isAddIntent: Boolean = intent.getBooleanExtra("is_add_intent", false)

        etEventTitle = findViewById(R.id.edit_text_event_upsert_name)
        etEventDescription = findViewById(R.id.edit_text_event_upsert_description)
        etEventDateTime = findViewById(R.id.edit_text_event_upsert_datetime)
        etEventSpeaker = findViewById(R.id.edit_text_event_upsert_speaker)
        etEventVenue = findViewById(R.id.edit_text_event_upsert_venue)
        etEventPrerequisites = findViewById(R.id.edit_text_event_upsert_prerequisites)
        ivEventPoster = findViewById(R.id.image_view_event_poster)

        etEventDateTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            // Create DatePickerDialog to select a date
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    // Create TimePickerDialog to select a time
                    val timePickerDialog = TimePickerDialog(
                        this,
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                            // Set the selected date and time on EditText
                            cal.set(Calendar.YEAR, year)
                            cal.set(Calendar.MONTH, monthOfYear)
                            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            cal.set(Calendar.MINUTE, minute)

                            val eventDateTimeText = Helpers.eventUpsertDateTimeFormat.format(cal.time)

                            etEventDateTime.setText(eventDateTimeText)
                            eventDate = cal.time
                        },
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        false
                    )
                    timePickerDialog.show()
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        upsertButton = findViewById(R.id.button_event_upsert)
        upsertButton.text = if (isAddIntent) "Add" else "Update"

        upsertButton.setOnClickListener {
            if (isAddIntent) {
                upsertButton.text = "Add"
                runBlocking { addCsiEvent() }
            } else {
                // Hopefully an update event
                upsertButton.text = "Update"
                updateCsiEvent()
            }
        }

        ivEventPoster.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, REQUEST_CODE_IMAGE_UPSERT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_UPSERT && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            ivEventPoster.setImageURI(imageUri)
        }
    }

    private suspend fun addCsiEvent() {
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image for the poster", Toast.LENGTH_SHORT).show()
            return
        }

        // Make an `Event` object out of the available details.
        // The `addEvent` wrapper takes care of the `EventId`.
        val event = Event(
            title = etEventTitle.text.toString(),
            venue = etEventVenue.text.toString(),
            speaker = etEventSpeaker.text.toString(),
            uuid = Helpers.generateEventUuid(),
            datetime = Helpers.generateUnixTimestampFromDate(Date()),
            description = etEventDescription.text.toString(),
        )

        runBlocking { EventWrapper.addEvent(event, imageUri) }
        Toast.makeText(applicationContext, "Added event", Toast.LENGTH_SHORT).show()

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun updateCsiEvent() {}
}