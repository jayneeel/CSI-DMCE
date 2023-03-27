package com.example.csi_dmce.events

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.EventAdapter
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.database.EventWrapper
import kotlinx.coroutines.runBlocking

class EventListActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_event_recyclerview)
        val events: List<Event> = runBlocking { EventWrapper.getEvents() }

        Log.d("LIST_EVENT_UI", events.size.toString())
        Log.d("LIST_EVENT_ONE", events[0].title.toString())
        Log.d("LIST_EVENT_TWO", events[1].title.toString())

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_events_list)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val adapter = EventAdapter(events)
        recyclerView.adapter = adapter
    }
}