package com.example.csi_dmce.complaint

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Complaint
import com.example.csi_dmce.database.ComplaintWrapper
import com.example.csi_dmce.database.StudentWrapper
import kotlinx.coroutines.runBlocking


class PreviousComplaintAdapter(private val complaintList : List<Complaint>): RecyclerView.Adapter<PreviousComplaintAdapter.MyViewHolder>() {
    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvDescription : TextView = itemView.findViewById(R.id.text_view_complaint_description)
        val tvSubject : TextView = itemView.findViewById(R.id.text_view_expense_expanded_event_name)
        val tvReply : TextView = itemView.findViewById(R.id.text_view_complaint_reply)

        val tvStudentName: TextView = itemView.findViewById(R.id.text_view_complaint_user_name)
        val tvStudentId: TextView = itemView.findViewById(R.id.text_view_complaint_user_student_id)
        val ivStudentAvatar: ImageView = itemView.findViewById(R.id.imageview_complaint_user_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.previous_complaint_card,parent,false);
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val complaint : Complaint = complaintList[position]
        holder.tvDescription.text = complaint.description
        holder.tvSubject.text = complaint.subject
        holder.tvStudentName.text = complaint.student_name
        holder.tvStudentId.text = complaint.student_id
        holder.tvReply.text = complaint.reply ?: "No reply received yet."

        runBlocking {
            StudentWrapper.getStudentAvatarUrl(complaint.student_id!!, complaint.avatar_extension) {
                Glide.with(holder.ivStudentAvatar.context)
                    .setDefaultRequestOptions(RequestOptions())
                    .load(it ?: R.drawable.ic_baseline_person_black_24)
                    .into(holder.ivStudentAvatar)
            }
        }
    }

    override fun getItemCount(): Int {
        return complaintList.size
    }
}
