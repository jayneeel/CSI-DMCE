package com.example.csi_dmce.Announcments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.R
import com.example.csi_dmce.dashboard.EventAdapter
import com.example.csi_dmce.utils.Helpers

class announcments_adapter(private val announcmentlist : ArrayList<AnnouncmentsWrapper> ) :
    RecyclerView.Adapter<announcments_adapter.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val title=itemView.findViewById<TextView>(R.id.textView11)
        val desc=itemView.findViewById<TextView>(R.id.textView13)
        val time=itemView.findViewById<TextView>(R.id.time)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.announcment_card,parent,false);
        return announcments_adapter.MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        return announcmentlist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val Announcment: AnnouncmentsWrapper=announcmentlist[position]
        holder.title.text=Announcment.title
        holder.desc.text=Announcment.description
        val timestamp=Announcment.time!!.toLong()
        holder.time.text= Helpers.generateDateFromUnixTimestamp(timestamp).toString()

    }
}