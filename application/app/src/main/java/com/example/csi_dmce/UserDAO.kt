package com.example.csi_dmce

import androidx.room.*

@Dao
interface UserDAO {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE user_id = :id")
    fun getUserById(id: Int): User

    @Query("SELECT * FROM user WHERE email= :log_email and password_hash= :log_password_hash")
    fun login_function(log_email: String, log_password_hash :String) : Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}