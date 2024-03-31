package com.example.csi_dmce.agenda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.R
import com.google.android.material.textview.MaterialTextView

class DoneAdapter(private val dataSet: List<String>) : RecyclerView.Adapter<DoneAdapter.DoneViewHolder>() {

    inner class DoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: MaterialTextView = itemView.findViewById(R.id.agenda_done_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoneViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_done_rv_item, parent, false)
        return DoneViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoneViewHolder, position: Int) {
        val dataItem = dataSet[position]
        holder.titleTextView.text = dataItem
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}
