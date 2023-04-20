package com.example.csi_dmce.events

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.EventAdapter
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CSIRole
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.database.EventWrapper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.runBlocking


class EventListActivity: AppCompatActivity() {
    // The requestCode for when we return to the list activity after adding or deleting an event.
    // In such a case, the view should re-render the RCV with the fresh/stale event objects.
    private val REQUEST_CODE_LOOP_TO_LIST: Int = 100

    private lateinit var btnEventAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_event_recyclerview)

        val userRole: CSIRole = CsiAuthWrapper.getRoleFromToken(applicationContext)

        Log.d("EVENTS", userRole.isAdmin().toString())

        btnEventAdd = findViewById(R.id.fab_admin_event_add)
        btnEventAdd.setOnClickListener {
            val intent = Intent(applicationContext, EventUpsertActivity::class.java)
            intent.putExtra("is_add_intent", true)
            startActivityForResult(intent, REQUEST_CODE_LOOP_TO_LIST)
        }

        if (!userRole.isAdmin()) {
           btnEventAdd.hide()
        }

        var events: List<Event> = runBlocking { EventWrapper.getEvents() }

        if (intent.getBooleanExtra("filter_favorites", false)) {
            events = filterFavorites(events)
            if (events.isEmpty()) {
                findViewById<TextView>(R.id.text_view_no_favorites)
                    .visibility = View.VISIBLE
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_events_list)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val adapter = EventAdapter(events)
        recyclerView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        finish()

        val intent = Intent(applicationContext, EventListActivity::class.java)
        startActivity(intent)
    }

    private fun filterFavorites(events: List<Event>): List<Event> {
        val filteredEvents: MutableList<Event> = mutableListOf()

        val favDbInstance = FavDbWrappers.returnFavDbInstance(applicationContext)
        val favEvents: List<String> = favDbInstance.favoriteDao().getAllFavorites()

        for (favEvent in favEvents) {
            filteredEvents.add(
                events.filter { it.eventId == favEvent }[0]
            )
        }

        return filteredEvents.toList()
    }
}