package com.example.csi_dmce

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.events.EventViewActivity
import com.example.csi_dmce.utils.Helpers

class EventAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    private lateinit var mContext: Context
    private val REQUEST_CODE_LOOP_TO_LIST: Int = 100

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvEventContainer: CardView = itemView.findViewById(R.id.card_view_event_container)
        val tvEventTitle: TextView = itemView.findViewById(R.id.text_view_rv_event_title)
        val tvEventDescription: TextView = itemView.findViewById(R.id.text_view_rv_event_description)
        val tvEventDate: TextView = itemView.findViewById(R.id.text_view_rv_event_date)
        val ivEventPoster: ImageView = itemView.findViewById(R.id.image_view_rv_event_poster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_event_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.cvEventContainer.setOnClickListener {
            val intent = Intent(holder.cvEventContainer.context, EventViewActivity::class.java)
            intent.putExtra("event_id", event.eventId)
            (mContext as Activity).startActivityForResult(intent, REQUEST_CODE_LOOP_TO_LIST)
        }
        holder.tvEventTitle.text = event.title
        holder.tvEventDescription.text = event.description
        holder.tvEventDate.text = Helpers.rvEventDateFormat.format(Helpers.generateDateFromUnixTimestamp(event.datetime!!))

        Glide.with(holder.ivEventPoster.context)
            .setDefaultRequestOptions(RequestOptions())
            .load(event.poster_url)
            .into(holder.ivEventPoster)
    }

    override fun getItemCount() = events.size
}