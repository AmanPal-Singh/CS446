package com.example.goosebuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Entity
@TypeConverters(DateTimeConverters::class)
data class CalendarItem (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val date: LocalDate?,
    val startTime: LocalTime?,
    val endTime: LocalTime?,
    val checked: Boolean,
)