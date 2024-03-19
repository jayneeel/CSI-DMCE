package com.example.csi_admin.expense

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Button
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
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking

class ExpenseRequest : AppCompatActivity() {
    private lateinit var etDatePicker : TextInputEditText
    private lateinit var etTotalCost : TextInputEditText
    private lateinit var etUpiId : TextInputEditText
    private lateinit var etExpenseTopic : TextInputEditText
    private lateinit var ivCurrentImage : ImageView

    private lateinit var tiTotalCost : TextInputLayout
    private lateinit var tiExpenseTopic : TextInputLayout
    private lateinit var tiDatePicker : TextInputLayout
    private lateinit var tiUpiId : TextInputLayout
    private lateinit var sendreq: Button

    private var imageUri: Uri? = null

    private lateinit var events: List<Event>
    private lateinit var selectedEvent: Event

    val REQUEST_CODE_IMAGE_PICKER = 104

    private lateinit var spinnerEventNames: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_request)

        events = runBlocking { EventWrapper.getEvents() }

        etTotalCost = findViewById(R.id.total_cost)
        tiTotalCost=findViewById(R.id.total_costcon)

        tiUpiId=findViewById(R.id.gpaycon)
        etUpiId = findViewById(R.id.googlePay_id)

        etExpenseTopic = findViewById(R.id.edit_text_expense_topic)
        tiExpenseTopic=findViewById(R.id.expensecon)
        spinnerEventNames = findViewById(R.id.spinner)

        etDatePicker=findViewById(R.id.date_expense)
        tiDatePicker=findViewById(R.id.datecon)
        sendreq=findViewById(R.id.sendexpensereq)

        //date picker
        val materialDateBuilder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
        val materialDatePicker = materialDateBuilder.build()
        materialDateBuilder.setTitleText("Select Date")
        etDatePicker.setOnClickListener {
            if (!materialDatePicker.isAdded){
                materialDatePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
                materialDatePicker.addOnPositiveButtonClickListener {
                    val selectedDateInMillis = it as Long
                    etDatePicker.setText(materialDatePicker.headerText)
                }}}

        constructExpenseSpinner()
        expensetopic()
        totalcost()
        date()
        gpay()

        sendreq.setOnClickListener {
            if(validation()){
              sendExpenseRequest()
               // Toast.makeText(this, "JINKLAS BHAVA", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validation(): Boolean {
        tiUpiId.helperText=validateupi()
        tiDatePicker.helperText=validateDATE()
        tiTotalCost.helperText=validatecost()
        tiExpenseTopic.helperText=validateexpense()

        val a=tiUpiId.helperText==null
        val b= tiDatePicker.helperText==null
        val c=tiTotalCost.helperText==null
        val d=tiExpenseTopic.helperText==null

        return a && b && c && d
    }

    private fun gpay() {
        etUpiId.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tiUpiId.helperText=validateupi()
            }
        }
    }

    private fun validateupi(): String? {
        if (etUpiId.text.toString().isEmpty()){
            return "Enter Upi ID"
        }
        else return null

    }

    private fun date() {
        etDatePicker.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tiDatePicker.helperText=validateDATE()
            }
        }
    }

    private fun validateDATE(): String? {
        if (etDatePicker.text.toString().isEmpty()){
            return "Enter Date"
        }
        else return null

    }

    private fun totalcost() {
        etTotalCost.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tiTotalCost.helperText=validatecost()
            }
        }
    }

    private fun validatecost(): String? {
        if (etDatePicker.text.toString().isEmpty()){
            return "Enter Cost"
        }
        else return null
    }

    private fun expensetopic() {
        etExpenseTopic.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                tiExpenseTopic.helperText=validateexpense()
            }
        }
    }

    private fun validateexpense(): String? {
        if (etExpenseTopic.text.toString().isEmpty()){
            return "Enter Topic"
        }
        else return null
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

    fun sendExpenseRequest() {
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