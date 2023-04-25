package com.example.csi_admin.complaint

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Complaint
import com.example.csi_dmce.database.ComplaintWrapper
import com.example.csi_dmce.database.StudentWrapper
import kotlinx.coroutines.runBlocking


class ComplaintAdapter(private val complaintList : List<Complaint>): RecyclerView.Adapter<ComplaintAdapter.MyViewHolder>() {

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvDescription : TextView = itemView.findViewById(R.id.text_view_complaint_reply)
        val tvSubject : TextView = itemView.findViewById(R.id.text_view_expense_expanded_event_name)
        val btnReply : Button = itemView.findViewById(R.id.reply_btn)

        val ivComplaintStatus : ImageView = itemView.findViewById(R.id.image_view_complaint_status)

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

        holder.ivComplaintStatus.setColorFilter(
            when(complaint.is_resolved) {
                true -> ContextCompat.getColor(holder.ivComplaintStatus.context, R.color.green)
                false -> ContextCompat.getColor(holder.ivComplaintStatus.context, R.color.golden)
                else -> ContextCompat.getColor(holder.ivComplaintStatus.context, R.color.golden)
            },
            PorterDuff.Mode.SRC_IN
        )


        holder.btnReply.setOnClickListener {
            val dialog = Dialog(holder.btnReply.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.complaint_reply_dialog)
            dialog.setCancelable(true)
            dialog.show()
            dialog.window?.setLayout(LayoutParams.MATCH_PARENT ,LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val etComplaintReply: EditText = dialog.findViewById(R.id.edit_text_complaint_reply)

            dialog
                .findViewById<Button>(R.id.button_complaint_dialog_cancel)
                .setOnClickListener { dialog.dismiss() }

            dialog
                .findViewById<Button>(R.id.button_complaint_dialog_reply)
                .setOnClickListener {
                    complaint.reply = etComplaintReply.text.toString()
                    complaint.is_resolved = true
                    runBlocking { ComplaintWrapper.addComplaint(complaint)}
                    Toast.makeText(it.context, "Reply sent successfully!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
        }

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