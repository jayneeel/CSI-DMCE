package com.example.csi_dmce.expense

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.csi_dmce.R
import com.example.csi_dmce.database.ApprovalStatus
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.database.EventWrapper
import com.example.csi_dmce.database.Expense
import com.example.csi_dmce.database.ExpensesWrapper
import com.example.csi_dmce.database.Student
import com.example.csi_dmce.database.StudentWrapper
import com.example.csi_dmce.utils.Helpers
import kotlinx.coroutines.runBlocking

class ExpenseAdapter(private val expenseList : ArrayList<Expense>): RecyclerView.Adapter<ExpenseAdapter.MyViewHolder>() {
    var userAvatarUrl: String? = null

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cvExpenseCard: CardView = itemView.findViewById(R.id.card_view_expense_approval)

        var tvStudentId: TextView = itemView.findViewById(R.id.user_student_id)
        val tvStudentName: TextView = itemView.findViewById(R.id.user_student_name)
        val tvExpenseCost: TextView = itemView.findViewById(R.id.text_view_expense_cost)

        val ivUserAvatar: ImageView = itemView.findViewById(R.id.image_view_expense_rcv_avatar)

        val btnExpenseDetails: Button = itemView.findViewById(R.id.button_expense_view_details)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.expense_approval_card, parent, false);
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val expenseObject: Expense = expenseList[position]
        val studentObject: Student =
            runBlocking { StudentWrapper.getStudent(expenseObject.student_id!!)!! }

        holder.cvExpenseCard.setOnClickListener {
            Toast.makeText(holder.cvExpenseCard.context, "Clicked on card", Toast.LENGTH_SHORT)
                .show()
        }

        holder.btnExpenseDetails.setOnClickListener {
            showDetailsDialog(holder.btnExpenseDetails.context, expenseObject, studentObject)
        }

        holder.tvStudentId.text = studentObject.student_id
        holder.tvStudentName.text = studentObject.name
        holder.tvExpenseCost.text = "₹ ${expenseObject.cost}"

        runBlocking {
            StudentWrapper.getStudentAvatarUrl(
                studentObject.student_id!!,
                studentObject.avatar_extension!!
            ) {
                Glide.with(holder.ivUserAvatar.context)
                    .setDefaultRequestOptions(RequestOptions())
                    .load(it ?: R.drawable.ic_baseline_person_black_24)
                    .into(holder.ivUserAvatar)

                userAvatarUrl = it
            }
        }
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    fun showDetailsDialog(ctx: Context, expenseObject: Expense, studentObject: Student) {
        val detailsDialog = Dialog(ctx)
        detailsDialog.setContentView(R.layout.expense_expanded_card)
        detailsDialog.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
        detailsDialog.setCancelable(true)
        detailsDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvStudentName: TextView =
            detailsDialog.findViewById(R.id.text_view_expense_expanded_student_name)
        tvStudentName.text = studentObject.name

        val tvStudentId: TextView =
            detailsDialog.findViewById(R.id.text_view_expense_expanded_student_id)
        tvStudentId.text = studentObject.student_id

        val tvAssociatedEvent: TextView =
            detailsDialog.findViewById(R.id.text_view_expense_expanded_event_name)

        val event: Event = runBlocking { EventWrapper.getEvent(expenseObject.associated_event!!)!! }
        tvAssociatedEvent.text = event.title

        val tvCost: TextView = detailsDialog.findViewById(R.id.text_view_expense_expanded_cost)
        tvCost.text = expenseObject.cost

        val tvExpenseDate: TextView = detailsDialog.findViewById(R.id.text_view_expense_date)
        tvExpenseDate.text = Helpers.expenseDateFormat.format(Helpers.generateDateFromUnixTimestamp(expenseObject.date_of_event!!))

        val tvUpiId: TextView = detailsDialog.findViewById(R.id.text_view_expense_expanded_upi_id)
        tvUpiId.text = expenseObject.upi_id

        val ivUserAvatar: ImageView = detailsDialog.findViewById(R.id.imageview_expense_expanded_user_avatar)
        Glide.with(ivUserAvatar.context)
            .setDefaultRequestOptions(RequestOptions())
            .load(userAvatarUrl ?: R.drawable.ic_baseline_person_black_24)
            .into(ivUserAvatar)

        detailsDialog
            .findViewById<Button>(R.id.button_dialog_approve_expense)
            .setOnClickListener {
                runBlocking {
                    ExpensesWrapper.setApprovalStatus(expenseObject, ApprovalStatus.Approved)
                }
                Toast.makeText(it.context, "Request approved!", Toast.LENGTH_SHORT).show()
                detailsDialog.dismiss()
            }

        detailsDialog
            .findViewById<Button>(R.id.button_dialog_decline_expense)
            .setOnClickListener {
                runBlocking {
                    ExpensesWrapper.setApprovalStatus(expenseObject, ApprovalStatus.Rejected)
                }
                Toast.makeText(it.context, "Request rejected!", Toast.LENGTH_SHORT).show()
                detailsDialog.dismiss()
            }

        val ivExpenseProof = detailsDialog.findViewById<ImageView>(R.id.image_view_expense_dialog_proof)
        val expenseProofUrl = runBlocking { ExpensesWrapper.getProofOfExpense(expenseObject) }
        Glide.with(ivExpenseProof.context)
            .setDefaultRequestOptions(RequestOptions())
            .load(expenseProofUrl)
            .into(ivExpenseProof)

        detailsDialog.show()
    }
}
