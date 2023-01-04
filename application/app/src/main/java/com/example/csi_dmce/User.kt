package com.example.csi_dmce

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val user_id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "password_hash")
    val password_hash: String = "",

    @Ignore
    val otp: Int = 0,

)