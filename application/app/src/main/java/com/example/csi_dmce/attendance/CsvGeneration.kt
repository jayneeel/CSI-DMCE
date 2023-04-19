package com.example.csi_dmce.attendance
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.csi_dmce.R
import com.example.csi_dmce.csv.EventSpinnerAdapter
import com.example.csi_dmce.database.Event
import com.example.csi_dmce.database.EventWrapper
import kotlinx.coroutines.runBlocking
import java.lang.reflect.Method

class CsvGeneration: AppCompatActivity() {

    private lateinit var attendanceCard : CardView
    private lateinit var expenseCard : CardView
    private lateinit var registrantCard : CardView

    private lateinit var btnDialogPositive: Button
    private lateinit var btnDialogNegative: Button

    private lateinit var selectedEvent: Event

    private lateinit var events: List<Event>

    private lateinit var spinnerEventNames: Spinner

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.csv_generation)

        events = runBlocking { EventWrapper.getEvents() }

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

        val dialog = Dialog(this, R.style.Theme_CSIDMCE)
        dialog.setContentView(R.layout.component_csv_attendance_popup)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnDialogNegative = dialog.findViewById(R.id.button_export_negative)
        btnDialogPositive = dialog.findViewById(R.id.button_export_positive)
        spinnerEventNames = dialog.findViewById(R.id.spinner_event_name)

        constructSpinner()

        attendanceCard.setOnClickListener {
            dialog.show()

            btnDialogPositive.setOnClickListener {
                val csvUri = AttendanceExportService.writeAttendanceData(this, selectedEvent.eventId!!)
                dialog.dismiss()
                openExcelSheet(csvUri)
            }

            btnDialogNegative.setOnClickListener {
                dialog.dismiss()
            }
        }

        expenseCard.setOnClickListener{
            val csvUri = ExpensesExportService.writeExpensesData(this)
            openExcelSheet(csvUri)
        }

        registrantCard.setOnClickListener{
            dialog.show()

            btnDialogPositive.setOnClickListener {
                val csvUri = RegistrationExportService.writeRegistrantsData(this, selectedEvent.eventId!!)
                dialog.dismiss()
                openExcelSheet(csvUri)
            }

            btnDialogNegative.setOnClickListener {
                dialog.dismiss()
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

    private fun constructSpinner() {
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