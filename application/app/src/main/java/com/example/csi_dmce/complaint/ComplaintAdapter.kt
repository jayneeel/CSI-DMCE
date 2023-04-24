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
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Complaint


class ComplaintAdapter(private val complaintList : List<Complaint>): RecyclerView.Adapter<ComplaintAdapter.MyViewHolder>() {

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvDescription : TextView = itemView.findViewById(R.id.complaint_text)
        val tvSubject : TextView = itemView.findViewById(R.id.text_view_expense_expanded_event_name)
        val btnReply : Button = itemView.findViewById(R.id.reply_btn)

        val tvStudentName: TextView = itemView.findViewById(R.id.text_view_complaint_user_name)
        val tvStudentId: TextView = itemView.findViewById(R.id.text_view_complaint_user_student_id)
        val ivStudentAvatar: ImageView = itemView.findViewById(R.id.imageview_complaint_user_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.complaint_card,parent,false);
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val complaint : Complaint = complaintList[position]
        holder.tvDescription.text = complaint.description
        holder.tvSubject.text = complaint.subject
        holder.tvStudentName.text = complaint.student_name
        holder.tvStudentId.text = complaint.student_id

        holder.btnReply.setOnClickListener {
            val dialog = Dialog(holder.btnReply.context)
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