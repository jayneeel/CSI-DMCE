package com.example.csi_dmce.agenda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.R

class PendingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PendingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pending, container, false)
        recyclerView = view.findViewById(R.id.pendingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Dummy data for testing
        val dummyData = listOf("Pending Item 1", "Pending Item 2", "Pending Item 3")

        adapter = PendingAdapter(dummyData) // Pass the dummy data to the adapter

        recyclerView.adapter = adapter
        return view
    }

}
