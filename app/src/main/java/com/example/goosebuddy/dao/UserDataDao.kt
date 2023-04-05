package com.example.goosebuddy.dao

import androidx.room.*
import com.example.goosebuddy.models.Habits
import com.example.goosebuddy.models.Routines
import com.example.goosebuddy.models.UserData

@Dao
interface UserDataDao {
    @Query("SELECT * FROM userdata")
    fun getAll(): UserData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userData: UserData)
    @Update
    fun update(userData: UserData)
}