package com.example.goosebuddy.dao

import androidx.room.*
import com.example.goosebuddy.models.CalendarItem
import com.example.goosebuddy.models.DateTimeConverters
import kotlinx.datetime.LocalDate

@Dao
@TypeConverters(DateTimeConverters::class)
interface CalendarItemDao {
    @Query("SELECT * FROM CalendarItem")
    fun getAll(): List<CalendarItem>

    @Query("SELECT * FROM CalendarItem WHERE date=:date ORDER BY startTime")
    fun getOnDate(date: LocalDate?): List<CalendarItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg calendarItem: CalendarItem)

    @Update
    fun update(calendarItem: CalendarItem)

    @Delete
    fun delete(calendarItem: CalendarItem)

    @Query("DELETE FROM CalendarItem WHERE seriesId=:seriesId")
    fun deleteInSeries(seriesId: Int)
}