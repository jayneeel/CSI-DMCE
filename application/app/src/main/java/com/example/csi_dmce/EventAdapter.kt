package com.example.csi_dmce

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class EventAdapter(private val eventlist : ArrayList<events>) :
    RecyclerView.Adapter<EventAdapter.MyViewHolder>() {

    private lateinit var mListner : onItemClickListner

    interface onItemClickListner{

        fun onItemClick(position: Int)

    }


    fun setOnItemClickListner(listner: onItemClickListner){

      mListner = listner

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_event_items,
        parent, false)
        return MyViewHolder(itemView,mListner)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = eventlist[position]
        holder.eventImage.setImageResource(currentItem.titleimage)
        holder.eventHeading.text = currentItem.eventname
        holder.date.text = currentItem.date
        holder.time.text = currentItem.time

    }

    override fun getItemCount(): Int {

        return eventlist.size
    }


    class MyViewHolder(itemView: View, listner: onItemClickListner) : RecyclerView.ViewHolder(itemView){

        val eventImage : ShapeableImageView = itemView.findViewById(R.id.event_image)
        val eventHeading : TextView = itemView.findViewById(R.id.event_heading)
        val date : TextView = itemView.findViewById(R.id.dateText)
        val time : TextView = itemView.findViewById(R.id.timeText)


        init{

            itemView.setOnClickListener {

                listner.onItemClick(adapterPosition)

            }

        }


    }


}