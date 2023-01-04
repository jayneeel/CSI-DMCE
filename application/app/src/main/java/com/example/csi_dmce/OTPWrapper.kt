package com.example.csi_dmce

data class OTP(
    val value: Int,
    val creation_time: Int,
    val expiration_time: Int,
    val isExpired: Boolean = false,
)

// TODO: Write a non-abstract implementation.
abstract class OTPWrapper {
    // TODO: Generate a random OTP and assign it to a specific user with a `user_id`.
    // Can we somehow serialize the OTP class and make it a part of our schema?
    abstract fun setOTP(user_id: Int)

    // Get OTP associated with a specific user with `user_id`.
    abstract fun getOTP(user_id: Int): OTP

    // Checks if a OTP is expired.
    abstract fun isExpired(otp: OTP): Boolean
}