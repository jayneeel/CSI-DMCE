package com.example.csi_dmce.dashboard

import android.annotation.SuppressLint
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

class EventFragment : Fragment() {
lateinit var eventSearchRecycler: RecyclerView
private lateinit var myAdapter: EventAdapter
private lateinit var filterBtn : CircleButton
private lateinit var eventArrayList : ArrayList<EventDataClass>
private lateinit var db : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_event, container, false)
        eventSearchRecycler = view.findViewById(R.id.eventSearchRecyclerView)
        filterBtn = view.findViewById(R.id.filter_Btn)
        filterBtn.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(view.context, filterBtn)
            popupMenu.menuInflater.inflate(R.menu.filter_menu, popupMenu.menu)
            popupMenu.show()
        }





            eventSearchRecycler.layoutManager= LinearLayoutManager(view.context)
        eventSearchRecycler.setHasFixedSize(true)
        eventArrayList = arrayListOf()
        myAdapter = EventAdapter(eventArrayList)
        eventSearchRecycler.adapter = myAdapter
        EventChangerListener()

        return view
    }

    private fun EventChangerListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("events").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error !=null){
                    Log.e("Firestore Error",error.message.toString())
                    return
                }
                for ( dc: DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        eventArrayList.add(dc.document.toObject(EventDataClass::class.java))
                    }
                }
                myAdapter.notifyDataSetChanged()
            }
        })
    }


}