package com.example.csi_dmce.notifications

import com.google.auth.oauth2.GoogleCredentials

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.csi_dmce.R
import com.example.csi_dmce.dashboard.DashMainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.StandardCharsets


class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Throws(IOException::class)


        private fun imageloader(c: String): Bitmap? {
        val `in`: InputStream
        try {
            val url = URL(c)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            `in` = connection.inputStream
            return BitmapFactory.decodeStream(`in`)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }




    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message data payload: \${remoteMessage.data}")
            // Show the notification
            val notificationBody = remoteMessage.getNotification()!!.body
            val notificationTitle = remoteMessage.getNotification()!!.title
            val notificationimage = remoteMessage.getNotification()!!.imageUrl
            val intent = remoteMessage.getNotification()!!.clickAction
            sendNotification(notificationTitle, notificationBody,notificationimage)
        }
    }


    @SuppressLint("ResourceAsColor")
      private  fun sendNotification(title: String?, body: String?, notificationimage: Uri?) {

          //NOTIFICATION BUILDER WHICH SHOWS THE NOTIFICATION IN THE NOTIFCATIONS SECTION
        val intent = Intent(this, DashMainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = "fcm_default_channel"
        Log.d(TAG, "sendNotification: $notificationimage")

        val bitmap=imageloader(notificationimage.toString())
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_csi_logo)
            .setColor(R.color.csi_blue)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "CSI_NOTIFICATIONS",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }



    companion object {
        @OptIn(DelicateCoroutinesApi::class)
        fun sendFCMMessage(title: String?, desc: String?, image: String,receiver: String) {
            GlobalScope.launch(Dispatchers.IO) {

                //API CALL API USED: FCM HTTP API V1
                var token= getAccessToken()
                Log.d(TAG, "sendFCMMessage: tokenitis  "+token)

                Log.d(TAG, "sendFCMMessage: "+image)

                val fcmEndpoint = "https://fcm.googleapis.com/v1/projects/csi-dmce-c6f11/messages:send"
                val url = URL(fcmEndpoint)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Authorization", "Bearer $token")
                connection.setRequestProperty("Content-Type", "application/json")

                // Construct JSON payload for FCM message
                val jsonPayload = """
            {
                "message": {
                    "topic": "$receiver",
                    "notification": {
                        "title": "$title",
                        "body": "$desc",
                        "image": "$image" 
                    }
                }
            }
        """.trimIndent()


                // Send JSON payload
                val outputStreamWriter = OutputStreamWriter(connection.outputStream)
                outputStreamWriter.write(jsonPayload)
                Log.d(TAG, "sendFCMMessage: **********************************")
                outputStreamWriter.flush()

                // Check response code
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Message sent successfully
                    Log.d(TAG, "sendFCMMessage: SUCSESSFULL"+responseCode.toString())
                } else {
                    // Handle error
                    Log.d(TAG, "sendFCMMessage: UNSUCSESSFULL"+responseCode.toString())
                    //401-AUTH ERROR
                }
            }
        }


        //Accessing token from firebase messsaging for authentication (cloud messaging)
        private fun getAccessToken(): String? {

        try {
        val json="""
            {
             
            } 
        """.trimIndent()

            val inputStream=ByteArrayInputStream(json.toByteArray(StandardCharsets.UTF_8))

            val MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging"
            val googleCredentials = GoogleCredentials.
            fromStream(inputStream).createScoped(listOf(MESSAGING_SCOPE))
            
            googleCredentials.refresh()
            val token=googleCredentials.getAccessToken().tokenValue
            Log.d(TAG, "getAccessToken: $token")
            return googleCredentials.getAccessToken().tokenValue


        }catch (e: Exception){
            Log.d(TAG, "getAccessToken: FAILED")
            return null
        }
        }

        private const val TAG = "MyFirebaseMsgService"
    }
}
