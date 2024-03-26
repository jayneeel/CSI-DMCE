package com.example.csi_dmce.dashboard

import ImageAdapter
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.database.Student
import com.example.csi_dmce.database.StudentWrapper
import com.example.csi_dmce.utils.Helpers
import com.example.csi_dmce.utils.ZenQuote
import com.example.csi_dmce.utils.ZenQuoteService
import com.google.firebase.firestore.*
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DashboardFragment : Fragment() {
    private lateinit var quoteOfTheDay: TextView

    lateinit var eventRecycler : RecyclerView
    lateinit var toolbar: Toolbar
    private lateinit var eventArrayList : ArrayList<Event>
    private lateinit var myAdapter: EventAdapter
    private lateinit var iAdapter: ImageAdapter
    lateinit var imageSlider: ImageSlider
    private lateinit var db : FirebaseFirestore
    var imageList2: ArrayList<String>? = null


    private lateinit var studentObject: Student

    private lateinit var tvDashWelcome: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_dashboard, container, false)
        var imageList = ArrayList<SlideModel>()
        val sharedPreferences = requireActivity().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)


        eventRecycler = view.findViewById(R.id.recyclerview)

        quoteOfTheDay = view.findViewById(R.id.text_view_quote_of_the_day)

        studentObject = runBlocking {
            StudentWrapper.getStudent(CsiAuthWrapper.getStudentId(view.context))!!
        }

        val studentName = if (studentObject.name != null) {
           studentObject.name!!.split(" ")[0]
        } else {
            "User"
        }

        //
        val announcments=view.findViewById<TextView>(R.id.announcment)
        announcments.isSelected=true
//        val paint=announcments.paint
//        val width=paint.measureText(announcments.text.toString())
//
//        announcments.paint.shader = LinearGradient(
//            0f, 0f, width, announcments.getTextSize(), intArrayOf(
//                Color.parseColor("#F97C3C"),
//                Color.parseColor("#FDB54E"),
//                Color.parseColor("#64B678"),
//                Color.parseColor("#478AEA"),
//                Color.parseColor("#8446CC")
//            ), null, Shader.TileMode.REPEAT
//        )



        tvDashWelcome = view.findViewById(R.id.text_view_dashboard_welcome)
        tvDashWelcome.setText("Welcome ${studentName}")

        eventRecycler.layoutManager= LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        eventRecycler.setHasFixedSize(true)
        eventArrayList = arrayListOf()

        myAdapter = EventAdapter(eventArrayList)
        eventRecycler.adapter = myAdapter
        EventChangerListener()

        imageSlider = view.findViewById(R.id.imageSlider)
        val storageReference: StorageReference = FirebaseStorage.getInstance().reference
        val image_refrance: StorageReference = storageReference.child("gallery")

        //val hashMap: HashMap<String, String> = HashMap()
        image_refrance.listAll().addOnSuccessListener(OnSuccessListener<ListResult> { listResult ->
            for (file in listResult.items) {
                file.getDownloadUrl()
                    .addOnSuccessListener { uri -> // adding the url in the arraylist
                        val dialog = Dialog(imageSlider.context)
                        var title = file.name.toString()
                        var url = uri.toString()
                        //hashMap.put(bb, uri.toString())
                        //println(hashMap)
                        imageList.add(SlideModel(url, title = title))
                        imageList.sortBy { it.title }
                        imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
                        imageSlider.setItemClickListener(object: ItemClickListener {
                            override fun onItemSelected(position: Int) {

                                dialog.setContentView(R.layout.component_image_scale_popup)
                                val ivFullScale = dialog.findViewById<ImageView>(R.id.image_view_fullscale)
                                var delete = dialog.findViewById<Button>(R.id.btn_delete)
                                Glide.with(ivFullScale.context)
                                    .setDefaultRequestOptions(RequestOptions())
                                    .load(imageList.get(position).imageUrl)
                                    .into(ivFullScale)

                                delete.setOnClickListener{
                                    imageList.removeAt(position)
                                    imageList.sortBy { it.title }
                                    imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
                                    dialog.dismiss()

                                    val editor = sharedPreferences.edit()
                                    editor.putString("imageList", Gson().toJson(imageList))
                                    editor.apply()

                                    // Retrieve the imageList from SharedPreferences when the app starts
                                    val savedImageListString = sharedPreferences.getString("imageList", null)
                                    if (savedImageListString != null) {
                                        val type = object : TypeToken<ArrayList<SlideModel>>() {}.type
                                        imageList = Gson().fromJson(savedImageListString, type)
                                    }
                                    imageList.sortBy { it.title }
                                    imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
                                }
                                dialog.show()
                                dialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
                            }
                        })
                        //Log.d("TAG", "onCreateView: url "+imageList2)
                        Log.d("TAG", "onCreateView: "+title+"="+url)
                        Log.d("TAG", "onCreateView: list2 "+imageList)
                    }
            }
            Log.d("TAG", "onCreateView: list3 "+imageList)
        })



        /*imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/csi-dmce-c6f11.appspot.com/o/gallery%2F1.jpg?alt=media&token=1b98937c-cf2c-4011-882a-c7985bb759fd", title = "Ideobition"))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/csi-dmce-c6f11.appspot.com/o/gallery%2F2.jpg?alt=media&token=2839c283-0eb6-4543-8ca5-6a8a188dbe02", title = "Time Travel"))
        imageList.add(SlideModel("https://firebasestorage.googleapis.com/v0/b/csi-dmce-c6f11.appspot.com/o/gallery%2F3.jpg?alt=media&token=7fe23dd0-7fb4-4c7a-8682-8b715ecfcb7c", title = "CSI"))
        Log.d("TAG", "onCreateView: "+imageList)*/


        setRandomQuote(view.context)

        return view
    }

    private fun setRandomQuote(ctx: Context) {
        val zenQuotesBaseurl = getString(R.string.zenquotes_daily_quote_url)

        // Check if we have a quote cached and serve it if TIME_ELAPSED > 24h
        // This is entirely done to avoid getting banned from Zenquotes.
        // Zenquotes updates their quote at 00:00AM CST.

        // The cached quote string has the format <UNIX_TIMESTAMP>__<QUOTE>
        val sharedPrefs = ctx.getSharedPreferences("csi_shared_prefs", Context.MODE_PRIVATE)
        val cachedQuoteString = sharedPrefs.getString("cached_quote", null)
        if (cachedQuoteString != null) {
            val (cacheTime, quote) = cachedQuoteString.split("__")
            // If 24 hours have not elapsed,
            if (Date().time < cacheTime.toLong() + Helpers.DAY_IN_MS) {
                Log.d("QUOTE_CACHE", "CACHE HIT")
                quoteOfTheDay.text = quote
                return
            }
        }

        val zenQuotesRetrofit: Retrofit = Retrofit.Builder()
            .baseUrl(zenQuotesBaseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val quoteService: ZenQuoteService = zenQuotesRetrofit
            .create(ZenQuoteService::class.java)

        val quoteCall: Call<List<ZenQuote>> = quoteService.getQuote()
        quoteCall.enqueue(object : Callback<List<ZenQuote>> {
            override fun onResponse(
                call: Call<List<ZenQuote>>,
                response: Response<List<ZenQuote>>
            ) {
                if (response.isSuccessful) {
                    val zenQuote = response
                        .body()
                        ?.get(0)
                        ?.q

                    quoteOfTheDay.text = zenQuote ?: resources.getString(R.string.default_quote)

                    // Cache the quote
                    ctx
                        .getSharedPreferences("csi_shared_prefs", Context.MODE_PRIVATE)
                        .edit()
                        .putString("cached_quote", "${Date().time}__${zenQuote!!}")
                        .apply()
                }
            }

            override fun onFailure(call: Call<List<ZenQuote>>, t: Throwable) {
                Log.d("FAILED", t.message.toString())
            }
        })
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