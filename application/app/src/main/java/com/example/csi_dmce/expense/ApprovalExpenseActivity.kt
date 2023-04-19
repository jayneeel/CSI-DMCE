package com.example.csi_admin.expense

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.R
import com.example.csi_dmce.database.Expense
import com.example.csi_dmce.database.ExpensesWrapper
import com.example.csi_dmce.expense.ExpenseAdapter
import kotlinx.coroutines.runBlocking

class ApprovalExpenseActivity : AppCompatActivity() {
    private lateinit var expenseObjects: List<Expense>

    private lateinit var rcvExpenseApproval: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approval_expense)

        expenseObjects = runBlocking { ExpensesWrapper.getExpensesObjects()!! }

        rcvExpenseApproval = findViewById(R.id.recyclerview_approval)
        val layoutManager = LinearLayoutManager(this)
        rcvExpenseApproval.layoutManager = layoutManager

        val adapter = ExpenseAdapter(expenseObjects as ArrayList<Expense>)
        rcvExpenseApproval.adapter = adapter
    }
}