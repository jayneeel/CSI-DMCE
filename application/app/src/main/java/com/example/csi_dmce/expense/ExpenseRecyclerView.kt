package com.example.csi_dmce.expense

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Expense
import com.example.csi_dmce.database.Student
import com.example.csi_dmce.database.StudentWrapper
import kotlinx.coroutines.runBlocking

class ExpenseAdapter(private val expenseList : ArrayList<Expense>): RecyclerView.Adapter<ExpenseAdapter.MyViewHolder>() {

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        // Components
        var tvStudentId: TextView = itemView.findViewById(R.id.user_student_id)
        val tvStudentName: TextView = itemView.findViewById(R.id.user_student_name)
        val tvExpenseCost: TextView = itemView.findViewById(R.id.text_view_expense_cost)

        val ivUserAvatar: ImageView = itemView.findViewById(R.id.image_view_expense_rcv_avatar)

        val btnExpenseApprove: Button = itemView.findViewById(R.id.button_dialog_approve_expense)
        val btnExpenseDecline: Button = itemView.findViewById(R.id.button_dialog_decline_expense)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.expense_approval_card,parent,false);
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val expenseObject : Expense = expenseList[position]
        val studentObject: Student = runBlocking { StudentWrapper.getStudent(expenseObject.student_id!!)!! }

        holder.btnExpenseDecline.setOnClickListener {
            // Decline
        }

        holder.btnExpenseApprove.setOnClickListener {
            // Approve
        }

        holder.tvStudentId.text = studentObject.student_id
        holder.tvStudentName.text = studentObject.name
        holder.tvExpenseCost.text = "₹ ${expenseObject.cost}"

        runBlocking {
            StudentWrapper.getStudentAvatarUrl(studentObject.student_id!!, studentObject.avatar_extension!!) {
                Glide.with(holder.ivUserAvatar.context)
                    .setDefaultRequestOptions(RequestOptions())
                    .load(it?: R.drawable.ic_baseline_person_black_24)
                    .into(holder.ivUserAvatar)
            }
        }
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }
}
