package com.example.goosebuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.goosebuddy.ui.screens.Subroutine

@Entity
data class Routines (
    // Represents the model for routines
    @PrimaryKey val id: Int,
    val title: String,
    val completedSteps: Int,
    val totalSteps: Int,
    val subroutine: Array<Subroutine>
)