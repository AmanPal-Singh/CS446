package com.example.goosebuddy.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.goosebuddy.models.Habits

@Dao
interface HabitsDao {
    @Query("SELECT * FROM habits")
    fun getAll(): List<Habits>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg habits: Habits)

    @Query("SELECT * FROM habits WHERE id=:id ")
    fun loadSingle(id: Int) : Habits

    @Delete
    fun delete(habits: Habits)

    @Query("DELETE FROM habits")
    fun deleteAll()

    @Update
    fun update(habits: Habits)
}