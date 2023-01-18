package com.example.csi_dmce.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    var user_id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "email")
    var email: String = "",

    @ColumnInfo(name = "password_hash")
    var password_hash: String = "",

    @Ignore
    var otp: Int = 0,
)
