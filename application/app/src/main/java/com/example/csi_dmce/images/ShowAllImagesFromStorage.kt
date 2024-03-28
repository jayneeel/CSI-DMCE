package com.example.csi_dmce.images

import ImageAdapterString
import com.example.csi_dmce.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference

class ShowAllImagesFromStorage : AppCompatActivity() {
    var imagelist: ArrayList<String>? = null
    var recyclerView: RecyclerView? = null
    var root: StorageReference? = null
    var progressBar: ProgressBar? = null
    var adapter: ImageAdapterString? = null
//    val toolbar: Toolbar = findViewById(R.id.toolbar)
//    val delete: MenuItem = findViewById(R.id.action_delete)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showallimages)
        imagelist = ArrayList()
        recyclerView = findViewById(R.id.recyclerview)
        adapter = ImageAdapterString(imagelist!!, this)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
        progressBar = findViewById(R.id.progress)
        progressBar!!.visibility = View.VISIBLE


        val storageReference: StorageReference = FirebaseStorage.getInstance().reference
        val image_refrance: StorageReference = storageReference.child("gallery")

        //val hashMap: HashMap<String, String> = HashMap()
        image_refrance.listAll().addOnSuccessListener(OnSuccessListener<ListResult> { listResult ->
            for (file in listResult.items) {
                file.getDownloadUrl()
                    .addOnSuccessListener { uri -> // adding the url in the arraylist
                        var bb = file.name.toString()
                        //hashMap.put(bb, uri.toString())
                        //println(hashMap)
                        imagelist!!.add(uri.toString())
                        Log.e("Itemvalue", bb+uri.toString())
                        imagelist!!.sort()
                    }.addOnSuccessListener {
                        recyclerView!!.setAdapter(adapter)
                        progressBar!!.setVisibility(View.GONE)
                    }
            }
        })
    }
}
