package com.example.csi_admin.expense

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.csv.EventSpinnerAdapter
import com.example.csi_dmce.database.ApprovalStatus
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.database.EventWrapper
import com.example.csi_dmce.database.Expense
import com.example.csi_dmce.database.ExpensesWrapper
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking

class ExpenseRequest : AppCompatActivity() {
    private lateinit var etDatePicker : TextInputEditText
    private lateinit var etTotalCost : TextInputEditText
    private lateinit var etUpiId : TextInputEditText
    private lateinit var etExpenseTopic : TextInputEditText
    private lateinit var ivCurrentImage : ImageView

    private var imageUri: Uri? = null

    private lateinit var events: List<Event>
    private lateinit var selectedEvent: Event

    val REQUEST_CODE_IMAGE_PICKER = 104

    private lateinit var spinnerEventNames: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_request)

        events = runBlocking { EventWrapper.getEvents() }

        etDatePicker = findViewById(R.id.date_expense)
        etDatePicker.setOnClickListener { datePicker() }

        etTotalCost = findViewById(R.id.total_cost);
        etUpiId = findViewById(R.id.googlePay_id)
        etExpenseTopic = findViewById(R.id.edit_text_expense_topic)

        spinnerEventNames = findViewById(R.id.spinner_expense_event_name)

        constructExpenseSpinner()
    }


    fun imagePicker(view: View){
        val cameraIntent = Intent()
        cameraIntent.type ="image/*"
        cameraIntent.action =Intent.ACTION_GET_CONTENT
        ivCurrentImage = view as ImageView
        startActivityForResult(cameraIntent, REQUEST_CODE_IMAGE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PICKER) {
            imageUri = data?.data
            ivCurrentImage.setImageURI(imageUri)
        }
    }

    fun datePicker() {
        val materialDateBuilder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
        val materialDatePicker = materialDateBuilder.build()
        materialDateBuilder.setTitleText("Select Date")
        materialDatePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        materialDatePicker.addOnPositiveButtonClickListener {
            etDatePicker.setText(materialDatePicker.headerText)
        }
    }

    fun sendExpenseRequest(view: View) {
        val expenseObject = Expense(
            student_id = CsiAuthWrapper.getStudentId(this),
            associated_event = selectedEvent.eventId,
            topic = etExpenseTopic.text.toString(),
            date_of_event = selectedEvent.datetime,
            cost = etTotalCost.text.toString(),
            upi_id = etUpiId.text.toString(),
            approval_status = ApprovalStatus.Pending.status,
        )

        if (imageUri == null) {
            Toast.makeText(this, "Please upload a proof of expense", Toast.LENGTH_SHORT).show()
            return
        }

        runBlocking { ExpensesWrapper.addExpense(expenseObject, imageUri!!) }

        Toast.makeText(this, "Request sent successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun constructExpenseSpinner() {
        val adapter = EventSpinnerAdapter(this, events)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEventNames.adapter = adapter

        spinnerEventNames.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedEvent = parent.getItemAtPosition(position) as Event
                Log.d("SELECTED", selectedEvent.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}