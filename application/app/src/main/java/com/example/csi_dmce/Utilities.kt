package com.example.csi_dmce

import java.math.BigInteger
import java.security.MessageDigest

class Utilities {
    // Prepare MD5 hash of a string
    fun get_md5_hash(plaintext: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(plaintext.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }
}