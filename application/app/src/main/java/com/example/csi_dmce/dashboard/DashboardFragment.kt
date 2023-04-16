package com.example.csi_dmce.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.example.csi_dmce.dashboard.EventAdapter
import com.example.csiappdashboard.EventDataClass
import com.google.firebase.firestore.*
import com.example.csi_dmce.R


class DashboardFragment : Fragment() {
lateinit var eventRecycler : RecyclerView
lateinit var toolbar: Toolbar
private lateinit var eventArrayList : ArrayList<EventDataClass>
private lateinit var myAdapter: EventAdapter
lateinit var imageSlider: ImageSlider
private lateinit var db : FirebaseFirestore
val imageList = ArrayList<SlideModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_dashboard, container, false)
        eventRecycler = view.findViewById(R.id.recyclerview)
//        toolbar = view.findViewById(R.id.navBar)

        eventRecycler.layoutManager= LinearLayoutManager(view.context)
        eventRecycler.setHasFixedSize(true)
        eventArrayList = arrayListOf()

        myAdapter = EventAdapter(eventArrayList)
        eventRecycler.adapter = myAdapter
        EventChangerListener()




        imageSlider = view.findViewById(R.id.imageSlider)
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/csi-dmce-c6f11.appspot.com/o/gallery%2F1.jpg?alt=media&token=1b98937c-cf2c-4011-882a-c7985bb759fd", title = "Ideobition"))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/csi-dmce-c6f11.appspot.com/o/gallery%2F2.jpg?alt=media&token=2839c283-0eb6-4543-8ca5-6a8a188dbe02", title = "Time Travel"))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/csi-dmce-c6f11.appspot.com/o/gallery%2F3.jpg?alt=media&token=7fe23dd0-7fb4-4c7a-8682-8b715ecfcb7c", title = "CSI"))
        imageSlider.setImageList(imageList)

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