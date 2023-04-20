package com.example.csi_dmce.dashboard

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.csi_dmce.dashboard.EventAdapter
import com.example.csiappdashboard.EventDataClass
import com.google.firebase.firestore.*
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.database.Student
import com.example.csi_dmce.database.StudentWrapper
import kotlinx.coroutines.runBlocking


class DashboardFragment : Fragment() {
    lateinit var eventRecycler : RecyclerView
    lateinit var toolbar: Toolbar
    private lateinit var eventArrayList : ArrayList<Event>
    private lateinit var myAdapter: EventAdapter
    lateinit var imageSlider: ImageSlider
    private lateinit var db : FirebaseFirestore
    val imageList = ArrayList<SlideModel>()

    private lateinit var studentObject: Student

    private lateinit var tvDashWelcome: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_dashboard, container, false)
        eventRecycler = view.findViewById(R.id.recyclerview)

        studentObject = runBlocking {
            StudentWrapper.getStudent(CsiAuthWrapper.getStudentId(view.context))!!
        }

        val studentName = if (studentObject.name != null) {
           studentObject.name!!.split(" ")[0]
        } else {
            "User"
        }

        tvDashWelcome = view.findViewById(R.id.text_view_dashboard_welcome)
        tvDashWelcome.setText("Welcome ${studentName}")

        eventRecycler.layoutManager= LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, true)
        eventRecycler.setHasFixedSize(true)
        eventArrayList = arrayListOf()

        myAdapter = EventAdapter(eventArrayList)
        eventRecycler.adapter = myAdapter
        EventChangerListener()

        imageSlider = view.findViewById(R.id.imageSlider)
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/csi-dmce-c6f11.appspot.com/o/gallery%2F1.jpg?alt=media&token=1b98937c-cf2c-4011-882a-c7985bb759fd", title = "Ideobition"))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/csi-dmce-c6f11.appspot.com/o/gallery%2F2.jpg?alt=media&token=2839c283-0eb6-4543-8ca5-6a8a188dbe02", title = "Time Travel"))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/csi-dmce-c6f11.appspot.com/o/gallery%2F3.jpg?alt=media&token=7fe23dd0-7fb4-4c7a-8682-8b715ecfcb7c", title = "CSI"))
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
        imageSlider.setItemClickListener(object: ItemClickListener {
            override fun onItemSelected(position: Int) {
                val dialog = Dialog(imageSlider.context)
                dialog.setContentView(R.layout.component_image_scale_popup)
                val ivFullScale = dialog.findViewById<ImageView>(R.id.image_view_fullscale)
                Glide.with(ivFullScale.context)
                    .setDefaultRequestOptions(RequestOptions())
                    .load(imageList.get(position).imageUrl)
                    .into(ivFullScale)

                dialog.show()
                dialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
            }
        })

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
                        eventArrayList.add(dc.document.toObject(Event::class.java))
                    }
                }
                myAdapter.notifyDataSetChanged()
            }

        })
    }
}