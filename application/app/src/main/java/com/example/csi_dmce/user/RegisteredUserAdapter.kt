package com.example.csi_dmce.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Student
import com.example.csi_dmce.database.StudentWrapper
import kotlinx.coroutines.runBlocking

class RegisteredUserAdapter(private val registeredUsersList : ArrayList<Student>): RecyclerView.Adapter<RegisteredUserAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val student_name : TextView = itemView.findViewById(R.id.user_student_name)
        val student_dept : TextView = itemView.findViewById(R.id.button_expense_expanded_deny)
        val student_id : TextView = itemView.findViewById(R.id.user_student_id)
        val student_avatar : ImageView = itemView.findViewById(R.id.image_view_expense_rcv_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_card,parent,false);
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user : Student = registeredUsersList[position]
        holder.student_id.text=user.student_id
        holder.student_dept.text = user.department
        holder.student_name.text = user.name

        runBlocking {
            StudentWrapper.getStudentAvatarUrl(user.student_id!!, user.avatar_extension!!){
                Glide.with(holder.student_avatar.context)
                    .setDefaultRequestOptions(RequestOptions())
                    .load(it?: R.drawable.ic_baseline_person_black_24)
                    .into(holder.student_avatar)
            }
        }

    }

    override fun getItemCount(): Int {
        return registeredUsersList.size
    }
}