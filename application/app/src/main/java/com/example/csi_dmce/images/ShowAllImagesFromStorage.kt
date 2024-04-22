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

    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    var adapter: ImageAdapterString? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showallimages)
        var imagelist = ArrayList<Photo>()
        recyclerView = findViewById(R.id.recyclerview)
        adapter = ImageAdapterString(imagelist, this)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
        progressBar = findViewById(R.id.progress)
        progressBar!!.visibility = View.VISIBLE



        val storageReference: StorageReference = FirebaseStorage.getInstance().reference
        val image_refrance: StorageReference = storageReference.child("gallery")

        image_refrance.listAll().addOnSuccessListener(OnSuccessListener<ListResult> { listResult ->
            for (file in listResult.items) {
                file.getDownloadUrl()
                    .addOnSuccessListener { uri -> // adding the url in the arraylist
                        var bb = file.name
                        imagelist.add(Photo(bb,uri.toString()))
                        Log.d("Itemvalue", bb+uri.toString())
                    }.addOnSuccessListener {
                        recyclerView!!.setAdapter(adapter)
                        progressBar!!.setVisibility(View.GONE)
                    }
            }
        })
    }
}
