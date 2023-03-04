package com.example.csi_dmce.events

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.database.EventWrapper
import com.example.csi_dmce.utils.Helpers
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.runBlocking


// TODO: Move the event card somewhere else, preferably in the dashboard.
class EventViewActivity: AppCompatActivity() {
    private lateinit var btnViewDetails: Button

    private lateinit var tvEventMinPoster: ImageView
    private lateinit var tvEventMinTitle: TextView
    private lateinit var tvEventMinDate: TextView
    private lateinit var tvEventMinTime: TextView
    private lateinit var tvEventMinVenue: TextView

    private lateinit var tvEventPoster: ImageView
    private lateinit var tvEventTitle: TextView
    private lateinit var tvEventMonth: TextView
    private lateinit var tvEventDay: TextView
    private lateinit var tvEventTime: TextView
    private lateinit var tvEventVenue: TextView
    private lateinit var tvEventDescription: TextView
    private lateinit var tvEventRegister: Button

    private var stateRegistered: Boolean = false

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

        tvEventMinPoster = layoutOne.findViewById(R.id.image_view_event_min_poster)
        tvEventMinTitle = layoutOne.findViewById(R.id.text_view_event_min_title)
        tvEventMinDate = layoutOne.findViewById(R.id.text_view_event_day)
        tvEventMinTime = layoutOne.findViewById(R.id.text_view_event_min_time)
        tvEventMinVenue = layoutOne.findViewById(R.id.text_view_event_min_venue)

        val eventDateTime = Helpers.generateDateFromUnixTimestamp(eventObject.datetime!!)

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.codeathon) // Replace with your placeholder image

        Glide.with(this)
            .setDefaultRequestOptions(requestOptions)
            .load(eventObject.poster_url)
            .into(tvEventMinPoster)

        tvEventMinTitle.setText(eventObject.title)
        tvEventMinVenue.setText(eventObject.venue)
        tvEventMinDate.setText(Helpers.eventDateMinFormat.format(eventDateTime))
        tvEventMinTime.setText(Helpers.eventTimeMinFormat.format(eventDateTime))

        val layoutTwo = inflater.inflate(R.layout.activity_view_event, container, false)
        tvEventPoster = layoutTwo.findViewById(R.id.image_view_event_poster)
        tvEventTitle = layoutTwo.findViewById(R.id.text_view_event_title)
        tvEventVenue = layoutTwo.findViewById(R.id.text_view_event_venue)
        tvEventTime = layoutTwo.findViewById(R.id.text_view_event_time)
        tvEventMonth = layoutTwo.findViewById(R.id.text_view_event_month)
        tvEventDay = layoutTwo.findViewById(R.id.text_view_event_day)
        tvEventDescription = layoutTwo.findViewById(R.id.text_view_event_description)
        tvEventRegister = layoutTwo.findViewById(R.id.button_event_register)
        tvEventRegister.setOnClickListener {
            MaterialAlertDialogBuilder(this, R.layout.component_event_confirmation_popup)
                .show()

            if (!stateRegistered) {
                tvEventRegister.text = "Registered"
                tvEventRegister.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.green)));
                val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_event_check_mark)
                tvEventRegister.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                stateRegistered = true
            } else {
                tvEventRegister.text = "Register"
                tvEventRegister.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.csi_primary_accent)));
                tvEventRegister.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                stateRegistered = false
            }
        }

        btnViewDetails = layoutOne.findViewById(R.id.button_event_min_view_details)
        btnViewDetails.setOnClickListener {
            container.removeAllViews()
            container.addView(layoutTwo)

            Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(eventObject.poster_url)
                .into(tvEventPoster)

            tvEventTitle.setText(eventObject.title)
            tvEventVenue.setText(eventObject.venue)
            tvEventTime.setText(Helpers.eventTimeFormat.format(eventDateTime))
            tvEventMonth.setText(Helpers.eventMonthFormat.format(eventDateTime))
            tvEventDay.setText(Helpers.eventDayFormat.format(eventDateTime))
            tvEventDescription.setText(eventObject.description)
        }
    }
}