package com.example.csi_dmce.agenda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.R
import com.google.android.material.textview.MaterialTextView

class PendingAdapter(private val dataSet: List<String>) : RecyclerView.Adapter<PendingAdapter.PendingViewHolder>() {

    inner class PendingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: MaterialTextView = itemView.findViewById(R.id.agenda_pending_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_pending_rv_item, parent, false)
        return PendingViewHolder(view)
    }

    override fun onBindViewHolder(holder: PendingViewHolder, position: Int) {
        val dataItem = dataSet[position]
        holder.titleTextView.text = dataItem
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}
