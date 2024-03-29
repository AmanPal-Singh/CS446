package com.example.goosebuddy.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.goosebuddy.models.WeekdayData

@Dao
interface WeekdayDataDao {
    @Query("SELECT * FROM weekdaydata")
    fun getAll(): List<WeekdayData>

    @Insert
    fun insertAll(vararg weekday_data: WeekdayData)

    @Delete
    fun delete(weekday_data: WeekdayData)
}