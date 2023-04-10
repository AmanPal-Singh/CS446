package com.example.goosebuddy.models.calendar

import androidx.room.*
import com.example.goosebuddy.models.DateTimeConverters
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = CalendarSeries::class,
        childColumns = ["seriesId"],
        parentColumns = ["id"]
    )]
)
@TypeConverters(DateTimeConverters::class)
data class CalendarItem (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val seriesId: Int?,
    val title: String,
    val date: LocalDate?,
    val startTime: LocalTime?,
    val endTime: LocalTime?,
    val checked: Boolean,
)