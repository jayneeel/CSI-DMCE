package com.example.csi_dmce.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.R
import com.example.csiappdashboard.EventDataClass
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventAdapter(private val eventList: ArrayList<EventDataClass>): RecyclerView.Adapter<EventAdapter.MyViewHolder>() {
    private val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titleEvent : TextView = itemView.findViewById(R.id.title)
        val dateEvent : TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_card,parent,false);
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event : EventDataClass = eventList[position]
        holder.titleEvent.text = event.title
        val dateText = getDateString(event.datetime)
        holder.dateEvent.text = dateText
    }

    private fun getDateString(datetime: Long?): CharSequence? {
        val dateTxt = simpleDateFormat.format(datetime?.times(1000L) ?: "")
        return dateTxt
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}