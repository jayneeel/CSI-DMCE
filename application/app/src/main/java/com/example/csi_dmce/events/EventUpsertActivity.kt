package com.example.csi_dmce.events

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.database.EventWrapper
import com.example.csi_dmce.utils.Helpers
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.*

class EventUpsertActivity: AppCompatActivity() {
    private lateinit var upsertButton: Button

    private lateinit var etEventTitle: EditText
    private lateinit var etEventDescription: EditText
    private lateinit var etEventDateTime: EditText
    private lateinit var etEventSpeaker: EditText
    private lateinit var etEventVenue: EditText
    private lateinit var etEventPrerequisites: EditText

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
    }

    private suspend fun addCsiEvent() {
        val event_poster_url = Firebase.storage.reference.child("sample.png").downloadUrl.await().toString()

        Log.d("EVENT_POSTER_URL", event_poster_url)


        // Make an `Event` object out of the available details.
        // The `addEvent` wrapper takes care of the `EventId`.
        val event = Event(
            title = etEventTitle.text.toString(),
            venue = etEventVenue.text.toString(),
            speaker = etEventSpeaker.text.toString(),
            uuid = Helpers.generateEventUuid(),
            datetime = Helpers.generateUnixTimestampFromDate(Date()),
            description = etEventDescription.text.toString(),
            poster_url = event_poster_url,
        )

        runBlocking { EventWrapper.addEvent(event) }
        Toast.makeText(applicationContext, "Added event", Toast.LENGTH_SHORT).show()

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun updateCsiEvent() {}
}