package com.example.goosebuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeekdayData(
    @PrimaryKey val id: Int,
    val weekday: String,
    val completedCount: Int,
    val totalCount: Int
)