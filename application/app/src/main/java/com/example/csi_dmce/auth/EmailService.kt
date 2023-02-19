package com.example.csi_dmce.auth

import android.content.Context
import com.example.csi_dmce.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailService {
    companion object {
        private fun getHtmlTemplate(emailKind: String, ctx: Context): String {
            val emailTemplateId: Int = when(emailKind) {
                "verification" -> R.raw.email_verification_template
                "password_reset" -> R.raw.reset_password_template
                else -> throw NotImplementedError("Template doesn't exist")
            }

            val htmlTemplate: String? = try {
                val inputStream = ctx.resources.openRawResource(emailTemplateId)
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                bufferedReader.use {
                    it.readText()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
            return htmlTemplate!!
        }

        suspend fun sendEmail(otp: String, emailKind: String, emailRecipient: String, ctx: Context): Boolean {
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

            var templateString: String = getHtmlTemplate(emailKind, ctx)
            templateString = templateString.replace("__EMAIL__", emailRecipient)
            templateString = templateString.replace("__OTP__", otp)

            try {
                val mimeMessage = MimeMessage(session).apply {
                    setFrom(InternetAddress(SMTP_EMAIL))
                    addRecipient(Message.RecipientType.TO, InternetAddress(emailRecipient))
                    subject = when(emailKind) {
                        "verification" -> "Verify Email address for CSI-DMCE"
                        "password_reset" -> "Reset your password for CSI-DMCE"
                        else -> throw NotImplementedError("Email kind not supported")
                    }
                    setContent(templateString, "text/html; charset=utf-8")
                }
                Transport.send(mimeMessage)
            } catch (e: MessagingException) {
                e.printStackTrace()
            }

            return true
        }
    }
}