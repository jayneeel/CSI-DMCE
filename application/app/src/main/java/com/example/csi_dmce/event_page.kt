package com.example.csi_dmce

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class event_page : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_page)

        val eventHeading : TextView = findViewById(R.id.text_Event)
        val eventVenu : TextView = findViewById(R.id.text_Venue)
        val eventImage : ImageView = findViewById(R.id.image_Event)
        val Eventdate : TextView = findViewById(R.id.text_date)
        val eventTime : TextView = findViewById(R.id.text_time)
        val eventDes : TextView = findViewById(R.id.text_des)
        val eventSpeaker : TextView = findViewById(R.id.text_speakers)
        val eventPrerequisites : TextView = findViewById(R.id.text_pre)


        val bundel : Bundle?= intent.extras
        val heading = bundel!!.getString("heading")
        val imageId = bundel.getInt("imageId")
        val venue = bundel.getString("venue")
        val dateId = bundel.getString("dateId")
        val timeId =bundel.getString("timeId")
        val description = bundel.getString("description")
        val speakers = bundel.getString("speakers")
        val prerequisites = bundel.getString("prerequisites")


        eventHeading.text = heading
        eventVenu.text = venue
        eventImage.setImageResource(imageId)
        Eventdate.text = dateId
        eventTime.text = timeId
        eventDes.text = description
        eventSpeaker.text = speakers
        eventPrerequisites.text = prerequisites




    }
}