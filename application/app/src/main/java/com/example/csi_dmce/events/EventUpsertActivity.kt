package com.example.csi_dmce.events

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.database.EventWrapper
import com.example.csi_dmce.notifications.MyFirebaseMessagingService
import com.example.csi_dmce.utils.Helpers
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import java.util.*

class EventUpsertActivity: AppCompatActivity() {
    private val REQUEST_CODE_IMAGE_UPSERT: Int = 101
    private var imageUri: Uri? = null

    private lateinit var oldEventObject: Event

    private lateinit var eventDate: Date

    private lateinit var upsertButton: Button
    private lateinit var cancelButton: Button

    private lateinit var etEventTitle: EditText
    private lateinit var etEventDescription: EditText
    private lateinit var etEventDateTime: EditText
    private lateinit var etEventSpeaker: EditText
    private lateinit var etEventVenue: EditText
    private lateinit var etEventPrerequisites: EditText
    private lateinit var ivEventPoster: ImageView

    private lateinit var tiEventTitle :TextInputLayout
    private lateinit var tiEventDescription :TextInputLayout
    private lateinit var tiEventDateTime :TextInputLayout
    private lateinit var tiEventSpeaker :TextInputLayout
    private lateinit var tiEventVenue :TextInputLayout
    private lateinit var tiEventPrerequisites :TextInputLayout

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_upsert)

        cancelButton = findViewById(R.id.button_event_upsert_cancel)
        cancelButton.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        val isAddIntent: Boolean = intent.getBooleanExtra("is_add_intent", false)

        etEventTitle = findViewById(R.id.edit_text_event_upsert_name)
        tiEventTitle = findViewById(R.id.namecon)

        etEventDescription = findViewById(R.id.edit_text_event_upsert_description)
        tiEventDescription = findViewById(R.id.desccon)

        etEventDateTime = findViewById(R.id.edit_text_event_upsert_datetime)
        tiEventDateTime = findViewById(R.id.datetimecon)

        etEventSpeaker = findViewById(R.id.edit_text_event_upsert_speaker)
        tiEventSpeaker = findViewById(R.id.speakercon2)

        etEventVenue = findViewById(R.id.edit_text_event_upsert_venue)
        tiEventVenue = findViewById(R.id.venuecon)

        etEventPrerequisites = findViewById(R.id.edit_text_event_upsert_prerequisites)
        tiEventPrerequisites = findViewById(R.id.prequisitecon)

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
            if (!isAddIntent) {
                oldEventObject = Gson()
                    .fromJson(intent.getStringExtra("update_event"), Event::class.java)

                etEventTitle.setText(oldEventObject.title)
                etEventDescription.setText(oldEventObject.description)
                etEventDateTime.setText(
                    Helpers.eventUpsertDateTimeFormat.format(
                        Helpers.generateDateFromUnixTimestamp(oldEventObject.datetime!!)
                    )
                )
                etEventSpeaker.setText(oldEventObject.speaker)
                etEventVenue.setText(oldEventObject.venue)
                etEventPrerequisites.setText(oldEventObject.prerequisites)

                val eventPosterUrl = runBlocking {
                    EventWrapper.getPosterUrl(
                        oldEventObject.eventId!!,
                        oldEventObject.poster_extension.toString()
                    )
                }
                Glide.with(this)
                    .setDefaultRequestOptions(RequestOptions())
                    .load(eventPosterUrl)
                    .into(ivEventPoster)
            }



        ivEventPoster.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, REQUEST_CODE_IMAGE_UPSERT)
        }

        titlee()
        desc()
        datetime()
        speaker()
        vemue()
        preq()

        upsertButton.setOnClickListener {
            if (validation()){
            if (isAddIntent) {
                runBlocking { addCsiEvent() }
            } else {
                // Hopefully an update event
                oldEventObject?.let {
                    runBlocking { updateCsiEvent(it) }
                }
            }}
        }

    }

    private fun preq() {
        etEventPrerequisites.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tiEventPrerequisites.helperText=validatepreq()
            }
        }
    }

    private fun validatepreq(): String? {
        if (etEventPrerequisites.text.toString().isEmpty()){
            return "Enter Prerequisites"
        }
        else return null
    }

    private fun vemue() {
        etEventVenue.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tiEventVenue.helperText=validatevenue()
            }
        }
    }

    private fun validatevenue(): String? {
        if (etEventVenue.text.toString().isEmpty()){
            return "Enter Venue"
        }
        else return null
    }

    private fun speaker() {
        etEventSpeaker.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tiEventSpeaker.helperText=validatespeaker()
            }
        }
    }

    private fun validatespeaker(): String? {
        if (etEventSpeaker.text.toString().isEmpty()){
            return "Enter Speaker Name"
        }
        else return null
    }

    private fun datetime() {
        etEventDateTime.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tiEventDateTime.helperText=validatedate()
            }
        }
    }

    private fun validatedate(): String? {
        if (etEventDateTime.text.toString().isEmpty()){
            return "Enter Date and Time"
        }
        else return null
    }

    private fun desc() {
        etEventDescription.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tiEventDescription.helperText=validatedesc()
            }
        }
    }

    private fun validatedesc(): String? {
        if (etEventDescription.text.toString().isEmpty()){
            return "Enter Description for the event"
        }
        else return null
    }

    private fun titlee() {
        etEventTitle.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tiEventTitle.helperText=validatetitle()
            }
        }
    }

    private fun validatetitle(): String? {
        if (etEventTitle.text.toString().isEmpty()){
            return "Enter Event Title"
        }
        else return null
    }

    private fun validation(): Boolean {
        tiEventTitle.helperText=validatetitle()
        tiEventPrerequisites.helperText=validatepreq()
        tiEventVenue.helperText=validatevenue()
        tiEventSpeaker.helperText=validatespeaker()
        tiEventDateTime.helperText=validatedate()
        tiEventDescription.helperText=validatedesc()

        val a= tiEventPrerequisites.helperText==null
        val b= tiEventVenue.helperText==null
        val c= tiEventSpeaker.helperText==null
        val d= tiEventDateTime.helperText==null
        val e=tiEventDescription.helperText==null
        val f= tiEventTitle.helperText==null

        return a && b && c&& d && e && f
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
            prerequisites = etEventPrerequisites.text.toString(),
            uuid = Helpers.generateEventUuid(),
            datetime = Helpers.generateUnixTimestampFromDate(
                Helpers.eventUpsertDateTimeFormat.parse(etEventDateTime.text.toString())!!
            ),
            description = etEventDescription.text.toString(),
        )

        runBlocking { EventWrapper.addEvent(event, imageUri) }
        Toast.makeText(applicationContext, "Added event", Toast.LENGTH_SHORT).show()

        setResult(Activity.RESULT_OK)
        finish()
    }

    private suspend fun updateCsiEvent(oldEventObject: Event) {
        var newEventObject: Event = oldEventObject.copy()

        newEventObject.title = etEventTitle.text.toString()
        newEventObject.description = etEventDescription.text.toString()
        newEventObject.datetime = Helpers.generateUnixTimestampFromDate(
            Helpers.eventUpsertDateTimeFormat.parse(
                etEventDateTime.text.toString()
            )!!
        )
        newEventObject.speaker = etEventSpeaker.text.toString()
        newEventObject.venue = etEventVenue.text.toString()
        newEventObject.prerequisites = etEventPrerequisites.text.toString()
        newEventObject.eventId = Helpers.createEventId(newEventObject.title!!, newEventObject.datetime!!)

        // If the date hasn't changed, then the ID will be same and we don't need to do any Firestore
        // stuff. Otherwise...
        if (oldEventObject.eventId != newEventObject.eventId) {
            if (imageUri == null) {
                // No new poster, just move the old one to the new path.
                runBlocking {
                    EventWrapper.moveEventStorage(oldEventObject.eventId!!, newEventObject.eventId!!, oldEventObject.poster_extension!!)
                    EventWrapper.deleteEventStorage(oldEventObject.eventId!!)
                    EventWrapper.addEvent(newEventObject)
                }
            } else {
                // New poster. Delete old one and insert new event.
                EventWrapper.deleteEventStorage(oldEventObject.eventId!!)
                runBlocking { EventWrapper.addEvent(newEventObject, imageUri) }
            }
        } else {
            runBlocking { EventWrapper.addEvent(newEventObject, imageUri) }
        }

        setResult(Activity.RESULT_OK)
        finish()
    }
}