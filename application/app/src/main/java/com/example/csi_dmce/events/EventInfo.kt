package com.example.csi_dmce.events

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.R
import com.example.csi_dmce.events.RegistrantAdapter
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.database.EventWrapper
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch


class EventInfo : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var nullTextView: TextView
    private lateinit var searchInput: TextInputEditText
    private lateinit var registrantAdapter: RegistrantAdapter // Declare the adapter as a property
    private var originalRegistrants: List<String> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_event_info)

        // Retrieve event title from intent extras
        val eventTitle = intent.getStringExtra("event_title")

        // Set event title to a TextView in the layout
        val tvEventInfoTitle = findViewById<TextView>(R.id.text_view_event_info_title)
        tvEventInfoTitle.text = eventTitle

        // Launch a coroutine to fetch the list of registrants
        lifecycleScope.launch {
            val registrants: List<String>? = getListOfRegistrants(eventTitle!!)
            registrants?.let { // Handle non-null case
                // Process the list of registrants
                recyclerView = findViewById(R.id.rv_event_info)
                recyclerView.layoutManager = LinearLayoutManager(this@EventInfo)

                registrantAdapter = RegistrantAdapter(registrants)
                recyclerView.adapter = registrantAdapter
                originalRegistrants = registrants // Store the original registrants list
                registrantAdapter.setOriginalRegistrants(originalRegistrants) // Set the original list to the adapter
            } ?: run {
                // Handle null case or provide a default list
                nullTextView = findViewById<TextView>(R.id.text_view_event_info_null)
                nullTextView.visibility = View.VISIBLE

            }
        }

        // Initialize search input and add text change listener
        searchInput = findViewById(R.id.search_area_event_info)
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isEmpty()) {
                    // If the query is empty, reset the RecyclerView to display the original list
                    registrantAdapter.setOriginalRegistrants(originalRegistrants)
                } else {
                    registrantAdapter.setOriginalRegistrants(originalRegistrants)
                    registrantAdapter.filterRegistrants(query) // Invoke filterRegistrants from the adapter
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })



        // Adjust padding to accommodate system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}



    suspend fun getListOfRegistrants(eventTitle: String): List<String>? {
        // Retrieve the list of events from Firestore
        val events = EventWrapper.getEvents()

        // Find the event with the matching title
        val matchingEvent = events.find { it.title == eventTitle }

        // If a matching event is found, return its registrants list
        return matchingEvent?.registrants
//        return null
    }

