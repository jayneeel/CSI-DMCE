package com.example.csi_dmce.auth.forgotpassword

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class EmailVerificationViewModel(application: Application) : AndroidViewModel(application) {
    val emailIsVerified = MutableLiveData<Boolean>()
    val emailId = MutableLiveData<String>()
}