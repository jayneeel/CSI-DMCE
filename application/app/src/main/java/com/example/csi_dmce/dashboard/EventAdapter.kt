package com.example.csi_dmce.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.database.EventWrapper
import com.example.csi_dmce.database.StudentWrapper
import com.example.csiappdashboard.EventDataClass
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventAdapter(private val eventList: ArrayList<Event>): RecyclerView.Adapter<EventAdapter.MyViewHolder>() {
    private val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titleEvent : TextView = itemView.findViewById(R.id.title)
        val dateEvent : TextView = itemView.findViewById(R.id.date)
        val ivEventPoster: ImageView = itemView.findViewById(R.id.event_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_card,parent,false);
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event : Event = eventList[position]
        holder.titleEvent.text = event.title
        val dateText = getDateString(event.datetime)
        holder.dateEvent.text = dateText

        val eventPosterUrl = runBlocking { EventWrapper.getPosterUrl(event.eventId!!, event.poster_extension!!) }

        Glide.with(holder.ivEventPoster.context)
            .setDefaultRequestOptions(RequestOptions())
            .load(eventPosterUrl)
            .into(holder.ivEventPoster)
    }

    private fun getDateString(datetime: Long?): CharSequence? {
        val dateTxt = simpleDateFormat.format(datetime?.times(1000L) ?: "")
        return dateTxt
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}