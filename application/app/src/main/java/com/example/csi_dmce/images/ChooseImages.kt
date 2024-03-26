package com.example.csi_dmce.images

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.csi_dmce.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import kotlin.collections.HashMap


class ChooseImages : AppCompatActivity() {

    lateinit var choose_img: Button
    lateinit var upload_img: Button
    lateinit var retrieve_img: Button
    lateinit var image_view: ImageView
    lateinit var title: TextInputEditText
    var fileUri: Uri? = null
//    val selectedImageUris: MutableList<Uri> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_choose_images)

        choose_img = findViewById(R.id.choose_image)
        upload_img = findViewById(R.id.upload_image)
        retrieve_img = findViewById(R.id.retrieve_image)
        image_view = findViewById(R.id.image_view)
        title = findViewById(R.id.enter_title)

        choose_img.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose Images to Upload"), 0
            )
        }

        upload_img.setOnClickListener {
            if (fileUri != null) {
                uploadImage()
            } else {
                Toast.makeText(
                    applicationContext, "Please Select Image to Upload",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        //Show All Images Function
        retrieve_img.setOnClickListener {
            val intent = Intent(applicationContext, ShowAllImagesFromStorage::class.java)
            startActivity(intent)
            //retrive_image()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
                image_view.setImageBitmap(bitmap)

            } catch (e: Exception) {
                Log.e("Exception", "Error: " + e)
            }
        }
    }


    fun uploadImage() {
        if (fileUri != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading Image...")
            progressDialog.setMessage("Processing...")
            progressDialog.show()

            val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                .child("gallery").child(title.text.toString())
            ref.putFile(fileUri!!).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "File Uploaded Successfully", Toast.LENGTH_LONG)
                    .show()
            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "File Upload Failed...", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}

//    private fun displayImage(imageUri: Uri) {
//        try {
//            Log.d("ChooseImages", "Displaying image: $imageUri")
//            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
//            image_view.setImageBitmap(bitmap)
//        } catch (e: Exception) {
//            Log.e("Exception", "Error: $e")
//        }
//    }







//    fun retrive_image() {
//        val storageReference: StorageReference = FirebaseStorage.getInstance().reference
//        val image_refrance: StorageReference = storageReference.child("gallery")
//
//        val hashMap: HashMap<String, String> = HashMap()
//        image_refrance.listAll().addOnSuccessListener(OnSuccessListener<ListResult> { listResult ->
//            for (file in listResult.items) {
//                file.getDownloadUrl()
//                    .addOnSuccessListener { uri -> // adding the url in the arraylist
//                       var bb = file.name.toString()
//                        hashMap.put(bb, uri.toString())
//                        println(hashMap)
//                        // imagelist.add(uri.toString())
//                        Log.e("Itemvalue", bb+uri.toString())
//                    }.addOnSuccessListener {
//                        //recyclerView.setAdapter(adapter)
//                        //progressBar.setVisibility(View.GONE)
//                    }
//            }
//        })
//
//        val progressDialog = ProgressDialog(this)
//        progressDialog.setTitle("Retriving Image...")
//        progressDialog.setMessage("Processing...")
//        progressDialog.show()
//
//        image_refrance.downloadUrl.addOnSuccessListener { uri: Uri ->
//
//            Glide.with(this@ChooseImages)
//                .load(uri)
//                .into(image_view)
//
//            progressDialog.dismiss()
//            Toast.makeText(this,"Image Retrived Successfull",Toast.LENGTH_LONG).show()
//        }
//            .addOnFailureListener { exception ->
//                progressDialog.dismiss()
//                Toast.makeText(this,"Image Retrived Failed: "+exception.message,Toast.LENGTH_LONG).show()
//
//            }
//    }
//

