package com.example.goosebuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Routines (
    // Represents the model for routines
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val completedSteps: Int,
    val totalSteps: Int
)