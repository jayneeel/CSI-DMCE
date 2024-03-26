package com.example.csi_dmce.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Student
import com.google.firebase.firestore.*

class UserListActivity : AppCompatActivity() {
    lateinit var recycler: RecyclerView
    private lateinit var usersArrayList: ArrayList<Student>
    private lateinit var myAdapter: RegisteredUserAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var iAdapter: RegisteredUserAdapter
    private lateinit var filteredUsersList: ArrayList<Student>
    private lateinit var searchView: SearchView
    private lateinit var usersCollection: CollectionReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        recycler = findViewById(R.id.userRecyclerView)
        searchView = findViewById(R.id.searchUsers)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

        usersArrayList = arrayListOf()
        filteredUsersList = arrayListOf()

        myAdapter = RegisteredUserAdapter(filteredUsersList)
        recycler.adapter = myAdapter

        db = FirebaseFirestore.getInstance()
        usersCollection = db.collection("users")

        setupSearchView()
        EventChangeListener()

    }

    private fun EventChangeListener() {
        usersCollection.addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore Error", error.message.toString())
                return@addSnapshotListener
            }
            usersArrayList.clear()
            for (doc in value!!) {
                val student = doc.toObject(Student::class.java)
                usersArrayList.add(student)
            }
            filter("") // Initially, display all users
        }
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filter(it) }
                return true
            }
        })
    }

    private fun filter(query: String) {
        filteredUsersList.clear()
        for (user in usersArrayList) {
            if (user.name?.contains(query, true) == true || user.student_id?.contains(query, true) == true || user.email?.contains(query, true) == true) {
                filteredUsersList.add(user)
            }
        }
        myAdapter.notifyDataSetChanged()
    }
}