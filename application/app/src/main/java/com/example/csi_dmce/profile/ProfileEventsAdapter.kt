package com.example.csi_dmce.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.R

class ProfileEventsAdapter (private val eventList : ArrayList<ProfileEvents>) : RecyclerView.Adapter<ProfileEventsAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.profile_events,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
       val currentItem = eventList[position]
        holder.titleHeading.text = currentItem.title_heading
        holder.description.text=currentItem.Description
    }

    override fun getItemCount(): Int
    {
        return eventList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val titleHeading : TextView = itemView.findViewById(R.id.Event)
        val description : TextView = itemView.findViewById(R.id.Description)
    }



}