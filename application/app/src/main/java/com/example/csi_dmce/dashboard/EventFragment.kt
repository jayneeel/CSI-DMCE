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
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EventFragment : Fragment() {
    lateinit var eventSearchRecycler: RecyclerView
    private lateinit var myAdapter: EventAdapter
    private lateinit var filterBtn : CircleButton
    private lateinit var eventArrayList : ArrayList<EventDataClass>
    private lateinit var db : FirebaseFirestore

    private val REQUEST_CODE_LOOP_TO_LIST: Int = 100

    private lateinit var btnEventAdd: FloatingActionButton
    private lateinit var shimmerContainer: ShimmerFrameLayout
    private lateinit var recyclerView: RecyclerView

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

        shimmerContainer = view.findViewById(R.id.shimmer_container)
        recyclerView = view.findViewById(R.id.recycler_view_events_list)

//        shimmerContainer.setShimmer(
//            Shimmer.AlphaHighlightBuilder() // You can customize the shimmer effect here
//                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
//                .setAutoStart(true)
//                .setBaseAlpha(0.2f)
//                .setHighlightAlpha(0.9f)
//                .setDuration(5000) // Adjust the duration of the shimmer animation
//                .build()
//        )

        shimmerContainer.startShimmer()
        GlobalScope.launch(Dispatchers.Main) {
//            delay(5000)
            val events: List<Event> = EventWrapper.getEvents()

            // Stop shimmer effect when data is loaded
            shimmerContainer.stopShimmer()
            shimmerContainer.visibility = View.GONE

            // Show RecyclerView with actual data
            recyclerView.visibility = View.VISIBLE
            recyclerView.layoutManager = LinearLayoutManager(view.context)
            recyclerView.adapter = com.example.csi_dmce.EventAdapter(events)
        }


        return view
    }
}