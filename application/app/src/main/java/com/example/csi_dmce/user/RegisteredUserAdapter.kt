package com.example.csi_admin.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_admin.R
import com.example.csi_admin.complaint.Complaint

class RegisteredUserAdapter(private val registeredUsersList : ArrayList<RegisteredUsers>): RecyclerView.Adapter<RegisteredUserAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val student_name : TextView = itemView.findViewById(R.id.user_student_name)
        val student_dept : TextView = itemView.findViewById(R.id.user_dept)
        val student_id : TextView = itemView.findViewById(R.id.user_student_id)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_card,parent,false);
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user : RegisteredUsers = registeredUsersList[position]
        holder.student_dept.text = user.department
        holder.student_name.text = user.name
    }

    override fun getItemCount(): Int {
        return registeredUsersList.size
    }
}