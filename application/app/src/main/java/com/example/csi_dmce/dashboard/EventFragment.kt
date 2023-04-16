package com.example.csi_dmce.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.markushi.ui.CircleButton
import com.example.csi_dmce.dashboard.EventAdapter
import com.example.csiappdashboard.EventDataClass
import com.google.firebase.firestore.*
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CSIRole
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.database.EventWrapper
import com.example.csi_dmce.events.EventUpsertActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.runBlocking

class EventFragment : Fragment() {
    lateinit var eventSearchRecycler: RecyclerView
    private lateinit var myAdapter: EventAdapter
    private lateinit var filterBtn : CircleButton
    private lateinit var eventArrayList : ArrayList<EventDataClass>
    private lateinit var db : FirebaseFirestore

    private val REQUEST_CODE_LOOP_TO_LIST: Int = 100

    private lateinit var btnEventAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.layout_event_recyclerview, container, false)
//        eventSearchRecycler = view.findViewById(R.id.eventSearchRecyclerView)
//        filterBtn = view.findViewById(R.id.filter_Btn)
//        filterBtn.setOnClickListener {
//            val popupMenu: PopupMenu = PopupMenu(view.context, filterBtn)
//            popupMenu.menuInflater.inflate(R.menu.filter_menu, popupMenu.menu)
//            popupMenu.show()
//        }
        val userRole: CSIRole = CsiAuthWrapper.getRoleFromToken(view.context)

        Log.d("EVENTS", userRole.isAdmin().toString())

        btnEventAdd = view.findViewById(R.id.fab_admin_event_add)
        btnEventAdd.setOnClickListener {
            val intent = Intent(view.context, EventUpsertActivity::class.java)
            intent.putExtra("is_add_intent", true)
            startActivityForResult(intent, REQUEST_CODE_LOOP_TO_LIST)
        }

        if (!userRole.isAdmin()) {
            btnEventAdd.hide()
        }

        val events: List<Event> = runBlocking { EventWrapper.getEvents() }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_events_list)
        val layoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = layoutManager

        val adapter = com.example.csi_dmce.EventAdapter(events)
        recyclerView.adapter = adapter

        return view
    }
}