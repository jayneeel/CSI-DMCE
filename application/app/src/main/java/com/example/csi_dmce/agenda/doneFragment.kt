package com.example.csi_dmce.agenda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.R

class DoneFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DoneAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_done, container, false)
        recyclerView = view.findViewById(R.id.doneRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Dummy data for testing
        val dummyData = listOf("Done Item 1", "Done Item 2", "Done Item 3")

        adapter = DoneAdapter(dummyData) // Pass the dummy data to the adapter

        recyclerView.adapter = adapter
        return view
    }

}
