package com.example.goosebuddy.dao

import androidx.room.*
import com.example.goosebuddy.models.Habits

@Dao
interface HabitsDao {
    @Query("SELECT * FROM habits")
    fun getAll(): List<Habits>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg habits: Habits)

    @Delete
    fun delete(habits: Habits)
}