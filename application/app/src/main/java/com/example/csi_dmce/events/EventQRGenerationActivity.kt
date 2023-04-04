package com.example.csi_dmce.events

import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.utils.GenerateQRHelper


class EventQRGenerationActivity: AppCompatActivity() {
    private lateinit var firstQr: ImageView
    private lateinit var secondQr: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_qr_generation)

        firstQr = findViewById(R.id.image_view_first_qr)
        secondQr = findViewById(R.id.image_view_second_qr)


        val qrDimensions: Pair<Int, Int> = calculateQrDimensions()

       val firstQrBmp: Bitmap = GenerateQRHelper.generateQr("FIRST_b932cc11-e621-4397-b686-446772f07d3e", qrDimensions)
        val secondQrBmp: Bitmap = GenerateQRHelper.generateQr("SECOND_645d5389-7530-4514-a185-9a86abb50fc8", qrDimensions)

        firstQr.setImageBitmap(firstQrBmp)
        secondQr.setImageBitmap(secondQrBmp)
    }

    private fun calculateQrDimensions(): Pair<Int, Int> {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels

        return Pair(
            screenWidth - 60,
            screenWidth - 60,
        )
    }

}