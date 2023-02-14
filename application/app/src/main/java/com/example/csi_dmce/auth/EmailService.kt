package com.example.csi_dmce.auth

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Properties
import javax.mail.*
import javax.mail.Message.RecipientType
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailService {
    companion object {
        suspend fun sendEmail(emailRecipient: String) {
            val smtpCredsRef = FirebaseFirestore
                .getInstance()
                .collection("credentials")
                .document("smtp")
                .get().await()

            val SMTP_EMAIL = smtpCredsRef.get("email").toString()
            val SMTP_PASSWORD = smtpCredsRef.get("password").toString()

            val props = Properties().apply {
                put("mail.smtp.host", "smtp.gmail.com")
                put("mail.smtp.socketFactory.port", "465")
                put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
                put("mail.smtp.auth", "true")
                put("mail.smtp.port", "465")
            }

            val session = Session.getDefaultInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(SMTP_EMAIL, SMTP_PASSWORD)
                }
            })

            try {
                val mimeMessage = MimeMessage(session).apply {
                    setFrom(InternetAddress("amitkulkarni2028@gmail.com"))
                    addRecipient(Message.RecipientType.TO, InternetAddress(emailRecipient))
                    subject = subject
                    setText("HEY THERE")
                }

                Transport.send(mimeMessage)
            } catch (e: MessagingException) {
                e.printStackTrace()
            }
        }
    }
}