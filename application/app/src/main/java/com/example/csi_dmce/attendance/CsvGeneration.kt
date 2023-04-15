package com.example.csi_dmce.attendance
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.csi_dmce.R
import java.io.File

class CsvGeneration: AppCompatActivity() {

    private lateinit var attendanceCard : CardView
    private lateinit var expenseCard : CardView
    private lateinit var registrantCard : CardView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.csv_generation)

        attendanceCard = findViewById(R.id.card_view_attendance_report)
        expenseCard = findViewById(R.id.card_view_expenses_report)
        registrantCard = findViewById(R.id.card_view_registrations_report)

        val popupView = layoutInflater.inflate(R.layout.csv_popup, null)
        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        val editText = popupView.findViewById<EditText>(R.id.eventID)
        val submitButton = popupView.findViewById<Button>(R.id.submit_button)

        attendanceCard.setOnClickListener {
            popupWindow.showAtLocation(attendanceCard, Gravity.CENTER, 0, 0)

            submitButton.setOnClickListener {
                val eventForCsv = editText.text.toString()
                val csvUri = AttendanceExportService.writeAttendanceData(this, eventForCsv)
                openExcelSheet(csvUri)
            }
        }

        expenseCard.setOnClickListener{
            val csvUri = ExpensesCSV.writeExpensesData(this)
        }

        registrantCard.setOnClickListener{
            popupWindow.showAtLocation(registrantCard, Gravity.CENTER, 0, 0)


            submitButton.setOnClickListener {
                val eventForCsv = editText.text.toString()
                RegistrantsCsv.writeRegistrantsData(this, eventForCsv)
            }
        }
    }

    private fun openExcelSheet(fileUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(fileUri, "application/vnd.ms-excel")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        if (intent.resolveActivity(applicationContext.packageManager) != null) {
            startActivity(intent)
        }
    }
}