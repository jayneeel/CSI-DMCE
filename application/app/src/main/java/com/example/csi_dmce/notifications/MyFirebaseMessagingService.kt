package com.example.csi_dmce.notifications
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import java.net.URL
import com.example.csi_dmce.R
import com.example.csi_dmce.dashboard.DashMainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.DelicateCoroutinesApi
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message data payload: \${remoteMessage.data}")
            // Show the notification
            val notificationBody = remoteMessage.getNotification()!!.body
            val notificationTitle = remoteMessage.getNotification()!!.title
            sendNotification(notificationTitle, notificationBody)
        }
    }

    private fun sendNotification(title: String?, body: String?) {
        val intent = Intent(this, DashMainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {
        @OptIn(DelicateCoroutinesApi::class)
        fun sendFCMMessage() {
            GlobalScope.launch(Dispatchers.IO) {
                val fcmEndpoint = "https://fcm.googleapis.com/v1/projects/csi-dmce-c6f11/messages:send"
                //val serverKey = "YOUR_SERVER_KEY"
                val url = URL(fcmEndpoint)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Authorization", "Bearer ya29.a0Ad52N39bHuBaZULyOv1VN7z-kfP6qb_Wu10cszsKLx0ISrQv9iRnoSQDd34_Ew7GcZS4Nr3n84nRyVG1U9oifQXOoLCh1qgXo0zxi9iwZYeZLmMGVBLokdSeNAVtE5EIGl7OgFjqUuFAaO9IrEN9-7oPvf3ZTr12jn15aCgYKAWISARESFQHGX2MiG8PhTy0DmW6RZycGLmefKg0171")
                connection.setRequestProperty("Content-Type", "application/json")
                Log.d(TAG, "sendFCMMessage: **********************************")
                // Construct JSON payload for FCM message
                val jsonPayload = """
            {
                "message": {
                    "topic": "all_users",
                    "notification": {
                        "title": "Your Title",
                        "body": "Your Message"
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
                    Log.d(TAG, "sendFCMMessage: SUCSESSFULL")
                } else {
                    // Handle error
                    Log.d(TAG, "sendFCMMessage: UNSUCSESSFULL"+responseCode.toString())
                }
            }
        }
        private const val TAG = "MyFirebaseMsgService"
    }
}
