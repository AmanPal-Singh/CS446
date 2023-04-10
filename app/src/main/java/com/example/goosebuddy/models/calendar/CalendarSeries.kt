package com.example.goosebuddy.models.calendar

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CalendarSeries (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val description: String,
)