package com.example.csi_dmce.images

import ImageAdapterString
import com.example.csi_dmce.R
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference

class ShowAllImagesFromStorage : AppCompatActivity() {
    var imagelist: ArrayList<String>? = null
    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null
    var adapter: ImageAdapterString? = null
    var deleteButton: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showallimages)
        imagelist = ArrayList()
        recyclerView = findViewById(R.id.recyclerview)
        adapter = ImageAdapterString(imagelist!!, this)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
        progressBar = findViewById(R.id.progress)
        progressBar!!.visibility = View.VISIBLE
        deleteButton = findViewById(R.id.deleteButton)


        val storageReference: StorageReference = FirebaseStorage.getInstance().reference
        val image_refrance: StorageReference = storageReference.child("gallery")

        image_refrance.listAll().addOnSuccessListener(OnSuccessListener<ListResult> { listResult ->
            for (file in listResult.items) {
                file.getDownloadUrl()
                    .addOnSuccessListener { uri -> // adding the url in the arraylist
                        var bb = file.name
                        imagelist!!.add(uri.toString())
                        Log.d("Itemvalue", bb+uri.toString())
                        imagelist!!.sort()
                    }.addOnSuccessListener {
                        recyclerView!!.setAdapter(adapter)
                        progressBar!!.setVisibility(View.GONE)
                    }
            }
        })

        deleteButton?.setOnClickListener {
            val selectedItems = adapter?.getSelectedItems()
            if (!selectedItems.isNullOrEmpty()) {
                for (index in selectedItems) {
                    // Remove the item from the list
                    val selectedList = ArrayList(selectedItems)
                    for (index in selectedList) {
                        // Remove the item from the list
                        imagelist?.removeAt(index)
                        // Notify adapter about the item removal
                        adapter?.notifyItemRemoved(index)
                    }
                }
                // Notify adapter about the item removal
                adapter?.notifyDataSetChanged()
                Toast.makeText(this, "Selected items deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No items selected", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
