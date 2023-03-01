package com.example.csi_dmce.events

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.database.EventWrapper
import com.example.csi_dmce.utils.Helpers
import kotlinx.coroutines.runBlocking


// TODO: Move the event card somewhere else, preferrably in the dashboard.
class EventViewActivity: AppCompatActivity() {
    private lateinit var btnViewDetails: Button

    /**
     * 1. When the activity starts, pop up the card.
     * 2. When clicked on 'View more details', take me to the event details page.
     *  - This should load all the event data from DB.
     *
     * 3. When clicked on register, transition and switch the button state to registered.
     *  - Create an attendance entry in the DB: collection=attendance.
     *  - Create an attendance entry in the DB: collection=user
     *  - Create an attendance entry in the DB: collection=event
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_event_container)

        // val eventId: String = intent.getStringExtra("event_id").toString()

        val eventId: String = "WoamCT-2839823829"
        val eventObject: Event = runBlocking { EventWrapper.getEvent(eventId)!! }

        val container: FrameLayout = findViewById(R.id.frame_layout_event_view_container)
        val inflater = LayoutInflater.from(this)
        val layoutOne = inflater.inflate(R.layout.component_event_cardview, container, false)
        container.addView(layoutOne)

        val tvEventMinPoster: ImageView = layoutOne.findViewById(R.id.image_view_event_min_poster)
        val tvEventMinTitle: TextView = layoutOne.findViewById(R.id.text_view_event_min_title)
        val tvEventMinDate: TextView = layoutOne.findViewById(R.id.text_view_event_min_date)
        val tvEventMinTime: TextView = layoutOne.findViewById(R.id.text_view_event_min_time)
        val tvEventMinVenue: TextView = layoutOne.findViewById(R.id.text_view_event_min_venue)

        val eventDateTime = Helpers.generateDateFromUnixTimestamp(eventObject.datetime!!)

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.codeathon) // Replace with your placeholder image

        Glide.with(this)
            .setDefaultRequestOptions(requestOptions)
            .load(eventObject.poster_url)
            .into(tvEventMinPoster)

        tvEventMinTitle.setText(eventObject.title)
        tvEventMinVenue.setText(eventObject.venue)
        tvEventMinDate.setText(Helpers.dateFormat.format(eventDateTime))
        tvEventMinTime.setText(Helpers.timeFormat.format(eventDateTime))

        btnViewDetails = layoutOne.findViewById(R.id.button_event_min_view_details)
        btnViewDetails.setOnClickListener {
            val layoutTwo = inflater.inflate(R.layout.activity_view_event, container, false)
            container.removeAllViews()
            container.addView(layoutTwo)
        }
    }
}