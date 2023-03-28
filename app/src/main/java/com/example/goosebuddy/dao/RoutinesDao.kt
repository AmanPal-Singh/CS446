package com.example.goosebuddy.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoutinesDao (
    // Represents the model for routines
    @PrimaryKey val id: Int,
    val title: String,
    val completedSteps: Int,
    val totalSteps: Int
)