package com.example.csi_dmce.attendance

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.RegistrationActivity
import com.example.csi_dmce.database.Attendance
import com.example.csi_dmce.database.AttendanceWrapper
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.runBlocking


class scan_qr : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scan_qr)

        val sharedPreferences =
            getSharedPreferences("KEY_DISABLE_AUTO_ORIENTATION", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val btnscan = findViewById<Button>(R.id.button)
        btnscan.setOnClickListener {
            editor.apply {
                putBoolean("KEY_DISABLE_AUTO_ORIENTATION", true)
                apply()
            }
            scanQR()
        }
    }

    private fun scanQR() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan a barcode")
        options.setCameraId(0) // Use a specific camera of the device
        options.setBeepEnabled(true)
        options.setBarcodeImageEnabled(true)
        options.captureActivity = (CaptureAct::class.java)
        barcodeLauncher.launch(ScanOptions())

    }

    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(
        ScanContract()
    ) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG)
                .show()
        }
    }
    }



class CaptureAct : CaptureActivity() {
}





