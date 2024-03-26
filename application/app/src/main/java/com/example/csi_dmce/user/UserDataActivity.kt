package com.example.csi_admin.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import com.example.csi_dmce.R
import com.example.csi_admin.complaint.ComplaintLodge
import com.example.csi_admin.complaint.ComplaintsActivity
import com.example.csi_admin.expense.ApprovalExpenseActivity
import com.example.csi_admin.expense.ExpenseRequest
import com.example.csi_dmce.attendance.CsvGeneration
import com.example.csi_dmce.user.UserListActivity

class UserDataActivity : AppCompatActivity() {
    private lateinit var cvExportSheets: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_data)

        cvExportSheets = findViewById(R.id.cardView9)
        cvExportSheets.setOnClickListener {
            val intent = Intent(this, CsvGeneration::class.java)
            startActivity(intent)
        }


    }

    fun complaintScreen(view: View) {
        val intent = Intent(this, ComplaintsActivity::class.java)
        startActivity(intent)
    }

    fun lodgeComplaintScreen(view: View) {
        val intent = Intent(this, ComplaintLodge::class.java)
        startActivity(intent)
    }

    fun userListScreen(view: View) {
        val intent = Intent(this, UserListActivity::class.java)
        startActivity(intent)
    }

    fun expenseApprovalScreen(view: View) {
        val intent = Intent(this, ApprovalExpenseActivity::class.java)
        startActivity(intent)
    }

    fun expenseRequestScreen(view: View) {
        val intent = Intent(this, ExpenseRequest::class.java)
        startActivity(intent)
    }

}