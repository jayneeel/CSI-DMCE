package com.example.csi_dmce.auth.forgotpassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.auth.EmailKind
import com.example.csi_dmce.auth.EmailService
import com.example.csi_dmce.database.StudentAuthWrapper
import com.example.csi_dmce.database.StudentWrapper
import com.example.csi_dmce.utils.Helpers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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
            val studentObject = runBlocking { StudentWrapper.getStudentByEmail(etEmailId.text.toString()) }

            if (studentObject == null) {
                Toast.makeText(requireContext().applicationContext, "This E-mail ID does not exist.", Toast.LENGTH_SHORT).show()
                btnSubmit.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext, com.otpview.R.color.red))
                btnSubmit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_wrong_cross, 0)
                btnSubmit.text = "Email ID doesn't exist!"
                Handler(Looper.getMainLooper()).postDelayed({
                    btnSubmit.text = "Submit"
                    btnSubmit.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext, R.color.csi_primary_accent))
                    btnSubmit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_next_arrow, 0)
                }, 1000)
                return@setOnClickListener
            }

            if (etEmailId.text.toString() != studentObject!!.email) {
                Toast.makeText(requireContext().applicationContext, "The entered e-mail ID doesn't match yours!", Toast.LENGTH_SHORT).show()
                requireActivity().setResult(Activity.RESULT_OK, Intent())
            }

            val otp = Helpers.generateOTP()
            runBlocking {
                StudentAuthWrapper.createForgotPasswordHashMap(studentObject.email!!, otp)
                launch {
                    withContext(Dispatchers.IO) {
                        EmailService.sendEmail(
                            otp,
                            EmailKind.RESET_PASSWORD_VERIFICATION,
                            studentObject.email!!,
                            requireContext().applicationContext
                        )
                    }
                }
            }

            val emailVerificationViewModel = activity?.run {
                ViewModelProvider(this).get(EmailVerificationViewModel::class.java)
            } ?: throw Exception("Invalid Activity")
            emailVerificationViewModel.emailId.value = etEmailId.text.toString()
            emailVerificationViewModel.emailIsVerified.value = true
        }
    }
}