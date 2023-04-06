package com.example.goosebuddy.dao

import androidx.room.*
import com.example.goosebuddy.models.CalendarItem
import com.example.goosebuddy.models.CalendarSeries

@Dao
interface CalendarSeriesDao {
    @Query("SELECT * FROM CalendarSeries")
    fun getAll(): List<CalendarSeries>

    @Query("SELECT * FROM CalendarSeries WHERE id=:id")
    fun getWithId(id: Int): List<CalendarSeries>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(calendarSeries: CalendarSeries): Long

    @Update
    fun update(calendarSeries: CalendarSeries)

    @Delete
    fun delete(calendarSeries: CalendarSeries)
}