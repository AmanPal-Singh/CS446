package com.example.goosebuddy.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.goosebuddy.models.Habits
import com.example.goosebuddy.models.Routines

@Dao
interface HabitsDao {
    @Query("SELECT * FROM habits")
    fun getAll(): List<Habits>

    @Insert
    fun insertAll(vararg habits: Habits)

    @Delete
    fun delete(habits: Habits)
}