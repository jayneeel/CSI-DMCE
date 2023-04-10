package com.example.csi_admin.complaint

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_admin.R


class ComplaintAdapter(private val complaintList : ArrayList<Complaint>): RecyclerView.Adapter<ComplaintAdapter.MyViewHolder>() {

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val desc : TextView = itemView.findViewById(R.id.complaint_text)
        val subject : TextView = itemView.findViewById(R.id.complaint_sub)
        val replyBtn : Button = itemView.findViewById(R.id.reply_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.complaint_card,parent,false);
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val complaint : Complaint = complaintList[position]
        holder.desc.text = complaint.description
        holder.subject.text = complaint.subject

        holder.replyBtn.setOnClickListener {
            Log.d("COMPLAINT","CLICKEDDD")

            val dialog = Dialog(holder.replyBtn.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.complaint_reply_dialog)
            dialog.setCancelable(true)
            dialog.show()
            dialog.window?.setLayout(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun getItemCount(): Int {
        return complaintList.size
    }


}