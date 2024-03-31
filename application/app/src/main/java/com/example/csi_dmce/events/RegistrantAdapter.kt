package com.example.csi_dmce.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.R
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.csi_dmce.database.StudentWrapper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class RegistrantAdapter(private var registrants: List<String>) :
    RecyclerView.Adapter<RegistrantAdapter.ViewHolder>() {
    private var originalRegistrants: MutableList<String> = mutableListOf()


    // Function to filter registrants based on the query


    fun filterRegistrants(query: String) {
        val filteredRegistrants = mutableListOf<String>()
        if (query.isEmpty()) {
            filteredRegistrants.addAll(originalRegistrants)
        } else {
            runBlocking {
                registrants.forEach { registrant ->
                    val registrantName = getRegistrantName(registrant)
                    val registrantDept = getRegistrantDept(registrant)
                    val registrantId = registrant // Assuming the ID is already available in the registrants list
                    if (registrantName != null && registrantDept != null && registrantId != null &&
                        (matchesQuery(registrant, query) || matchesQuery(registrantName, query) ||
                                matchesQuery(registrantDept, query) || matchesQuery(registrantId, query))
                    ) {
                        filteredRegistrants.add(registrant)
                    }
                }
            }
        }
        updateRegistrants(filteredRegistrants)
    }

    fun setOriginalRegistrants(registrants: List<String>) {
        originalRegistrants = registrants.toMutableList()
        updateRegistrants(originalRegistrants)
    }


    private fun matchesQuery(value: String, query: String): Boolean {
        return value.contains(query, ignoreCase = true)
    }


    fun updateRegistrants(filteredRegistrants: List<String>) {
        registrants = filteredRegistrants
        notifyDataSetChanged()
    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRegistrantId: TextView = itemView.findViewById(R.id.event_info_student_id)
        val tvRegistrantName: TextView = itemView.findViewById(R.id.event_info_student_name)
        val tvRegistrantDept: TextView = itemView.findViewById(R.id.event_info_student_dept)
        val rivRegistrantAvatar: RoundedImageView = itemView.findViewById(R.id.event_info_avatar)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_view_event_info_item, parent, false) // Use your layout file name here
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val registrant = registrants[position]
        holder.tvRegistrantId.text = registrant
        // Initialize the avatar ImageView with the placeholder image
        holder.rivRegistrantAvatar.setImageResource(R.drawable.ic_baseline_person_black_24)



        GlobalScope.launch(Dispatchers.Main) {
            val registrantName = getRegistrantName(registrant)
            // Update the UI with the registrant name
            holder.tvRegistrantName.text = registrantName ?: "Unknown"

            val tvRegistrantDept = getRegistrantDept(registrant)
            //Update the UI with registrant dept
            holder.tvRegistrantDept.text = tvRegistrantDept ?: "Unknown"

            val avatarExtension = getAvatarExtensionForRegistrant(registrant)

            // Load the profile picture from Firebase Storage using the registrant ID and avatar extension
            val storageReference = FirebaseStorage.getInstance().reference
            val imageRef = storageReference.child("users/$registrant/avatar.$avatarExtension")

            imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                // Load the profile picture into the ImageView using Glide
                Glide.with(holder.rivRegistrantAvatar.context)
                    .load(imageUrl)
                    .apply(RequestOptions().placeholder(R.drawable.ic_baseline_person_black_24)) // Placeholder image while loading
                    .into(holder.rivRegistrantAvatar)
            }.addOnFailureListener { exception ->
                // Handle any errors while retrieving the image
                Log.e("RegistrantAdapter", "Error loading image for registrant $registrant: $exception")
            }


        }


    }



//    override fun getItemCount() = registrants.size

    override fun getItemCount(): Int {
        val itemCount = registrants.size
        Log.d("RegistrantAdapter", "Number of registrants: $itemCount")
        return itemCount
    }

    // Function to fetch the name of a registrant from Firestore using their ID
    private suspend fun getRegistrantName(registrantId: String): String? {
        val firestore = FirebaseFirestore.getInstance()
        val userDoc = firestore.collection("users").document(registrantId).get().await()
        return userDoc.getString("name")

    }

    private suspend fun getRegistrantDept(registrantId: String): String? {
        val firestore = FirebaseFirestore.getInstance()
        val userDoc = firestore.collection("users").document(registrantId).get().await()
        return userDoc.getString("department")
    }

    private suspend fun getAvatarExtensionForRegistrant(registrantId: String): String? {
        val firestore = FirebaseFirestore.getInstance()
        val userDoc = firestore.collection("users").document(registrantId).get().await()
        return userDoc.getString("avatar_extension")

    }



}
