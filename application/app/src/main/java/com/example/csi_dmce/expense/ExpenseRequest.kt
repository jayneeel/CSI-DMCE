package com.example.csi_admin.expense

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.csi_admin.R
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class ExpenseRequest : AppCompatActivity() {
    private val db : FirebaseFirestore by lazy { Firebase.firestore }
    val materialDateBuilder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
    lateinit var datePickerEt : TextInputEditText
    lateinit var associatedEventEt : TextInputEditText
    lateinit var totalCostEt : TextInputEditText
    lateinit var upiEt : TextInputEditText
    lateinit var descriptionEt : TextInputEditText
    lateinit var currentImageView : ImageView
    val pic_id = 123
    val storageRef = FirebaseStorage.getInstance().reference




    val materialDatePicker = materialDateBuilder.build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_request)
        materialDateBuilder.setTitleText("Select Date")
        datePickerEt= findViewById(R.id.date_expense)
        associatedEventEt= findViewById(R.id.associated_event)
        totalCostEt= findViewById(R.id.total_cost);
        upiEt= findViewById(R.id.googlePay_id)
        descriptionEt = findViewById(R.id.expense_description)
    }


    fun imagePicker(view: View){
        val cameraIntent = Intent()
        cameraIntent.type="image/*"
        cameraIntent.action=Intent.ACTION_GET_CONTENT
        currentImageView = view as ImageView
        startActivityForResult(cameraIntent, pic_id)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pic_id) {
            val uri = data?.data
            val filename = System.currentTimeMillis().toString()
            val imageRef = storageRef.child("expenses/$filename.jpg")
            Log.d("URI TEST",uri.toString())
            if (uri != null) {

                imageRef.putFile(uri).addOnSuccessListener { taskSnapshot ->
                    val downloadUrl = taskSnapshot.metadata?.reference?.downloadUrl
                    Log.d("URI TEST","****** \n$downloadUrl\n ********")
                }
                    .addOnFailureListener {
                        Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show()
                    }
            }
           currentImageView.setImageURI(uri)
        }
    }


    fun datePicker(view: View) {
        materialDatePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        materialDatePicker.addOnPositiveButtonClickListener {
            datePickerEt.setText(materialDatePicker.headerText)
        }
    }

    fun sendExpenseRequest(view: View) {
        val eventTxt = associatedEventEt.text.toString()
        val descriptionTxt = descriptionEt.text.toString()
        val dateTxt = datePickerEt.text.toString()
        val totalCostTxt = totalCostEt.text.toString()
        val upiTxt = upiEt.text.toString()

        val map = mutableMapOf<String,String>()
        map.put("associated_event",eventTxt)
        map.put("description_of_event",descriptionTxt)
        map.put("total_cost",totalCostTxt)
        map.put("date_of_event",dateTxt)
        map.put("upi_id",upiTxt)
        map.put("proofs","")
        db.collection("expenses").add(map)
            .addOnSuccessListener {
                Toast.makeText(this,"Request Sent Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ExpenseApproval::class.java)
                startActivity(intent)
            }
            .addOnFailureListener{
                Toast.makeText(this,"Error $it", Toast.LENGTH_SHORT).show()
            }
    }

}