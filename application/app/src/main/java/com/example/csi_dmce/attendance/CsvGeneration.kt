package com.example.csi_dmce.attendance
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.csi_dmce.R
import java.lang.reflect.Method

class CsvGeneration: AppCompatActivity() {

    private lateinit var attendanceCard : CardView
    private lateinit var expenseCard : CardView
    private lateinit var registrantCard : CardView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.csv_generation)

        val packageName = packageName
        val pm = packageManager

        if (Environment.isExternalStorageManager()){
            Log.d("CSI_PERMS", "ALREADY GRANTED")
        } else {
            Log.d("CSI_PERMS", "REQUESTING")
            // The MANAGE_EXTERNAL_STORAGE permission has not been granted.
            // Your app needs to request this permission.
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            val uri: Uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }

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
            Log.d("CSV", csvUri.toString())
            openExcelSheet(csvUri)
        }

        registrantCard.setOnClickListener{
            popupWindow.showAtLocation(registrantCard, Gravity.CENTER, 0, 0)


            submitButton.setOnClickListener {
                val eventForCsv = editText.text.toString()
                val csvUri = RegistrantsCsv.writeRegistrantsData(this, eventForCsv)
                openExcelSheet(csvUri)
            }
        }
    }

    private fun openExcelSheet(fileUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(fileUri, "text/csv")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val chooser = Intent.createChooser(intent, "Open CSV file with...")
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                val m: Method = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        startActivity(chooser)
    }
}