package com.example.csi_dmce.auth.forgotpassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.auth.EmailService
import com.example.csi_dmce.database.StudentWrapper
import com.example.csi_dmce.utils.Helpers
import kotlinx.coroutines.runBlocking

class ForgotPasswordVerifyEmailFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password_verify_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etEmailId: EditText = view.findViewById(R.id.edit_text_fop_email_id)
        val btnSubmit: Button = view.findViewById(R.id.button_fop_submit_email)

        btnSubmit.setOnClickListener {
            val decodedToken: DecodedJWT = CsiAuthWrapper.parseAuthToken(requireContext().applicationContext)
            val studentId = decodedToken.getClaim("student_id").asString()
            val studentObject = runBlocking { StudentWrapper.getStudent(studentId) }

            if (etEmailId.text.toString() != studentObject!!.email) {
                Toast.makeText(requireContext().applicationContext, "The entered e-mail ID doesn't match yours!", Toast.LENGTH_SHORT).show()
                requireActivity().setResult(Activity.RESULT_OK, Intent())
            }

            val otp = Helpers.generateOTP()
            runBlocking {
                EmailService.sendEmail(
                    otp,
                    "reset_password",
                    studentObject.email!!,
                    requireContext().applicationContext
                )
            }

            val emailVerificationViewModel = activity?.run {
                ViewModelProvider(this).get(EmailVerificationViewModel::class.java)
            } ?: throw Exception("Invalid Activity")
            emailVerificationViewModel.emailIsVerified.value = true
        }
    }
}