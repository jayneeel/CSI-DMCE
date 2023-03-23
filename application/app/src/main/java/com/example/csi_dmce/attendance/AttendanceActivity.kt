package com.example.csi_dmce.attendance

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.csi_dmce.MainActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.database.*
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.runBlocking

class AttendanceActivity: AppCompatActivity() {
    // var qrContents: String? = "FIRST_ac86c6f9-61dc-4a32-8950-037e51c25d4c"
    val studentID = "2019FHCO106"
    //where to get the eventID from?
    // val eventId : String = intent.getStringExtra("event_id").toString()
    val eventId : String = "CSCT-1678280388"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences =
            getSharedPreferences("KEY_DISABLE_AUTO_ORIENTATION", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.apply {
            putBoolean("KEY_DISABLE_AUTO_ORIENTATION", true)
            apply()
        }

        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan a barcode")
        options.setCameraId(0) // Use a specific camera of the device
        options.setBeepEnabled(true)
        options.setBarcodeImageEnabled(true)
        options.captureActivity = (CaptureAct::class.java)
        barcodeLauncher.launch(options)
    }

    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(
        ScanContract()
    ) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG)
                .show()

            val qrContents = result.contents
            val eventUUID: String =  runBlocking  { EventWrapper.getEventUuid(eventId)!!}

            val qrType: String = qrContents!!.split("_")[0].lowercase()
            val qrUuid: String = qrContents!!.split("_")[1]

            if (qrUuid != eventUUID) {
                Toast.makeText(applicationContext, "Wrong QR!", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }

            var attendanceObject: Attendance
            runBlocking {
                AttendanceWrapper.setQRAttendance(studentID, eventUUID, qrType, true)
                attendanceObject = AttendanceWrapper.getAttendanceObject(studentID, eventUUID)!!
            }

            val first : Boolean? = attendanceObject.first
            val second : Boolean? = attendanceObject.second

            if(first==true && qrType == "second") {
                Log.d("ATTENDANCE", "registering attendance!")
                runBlocking {
                    val studentObj : Student = StudentWrapper.getStudent(studentID)!!
                    Log.d("TAG", eventId)
                    val eventObj: Event =  EventWrapper.getEvent(eventId)!!
                    CommonWrapper.addAttendee(eventObj, studentObj)
                }
            }

            setContentView(R.layout.attendance_ok)
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 1000)

        }
    }

    class CaptureAct : CaptureActivity()
}
