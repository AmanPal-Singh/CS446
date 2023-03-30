package com.example.goosebuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habits (
    // Represents the model for habits
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
    var description: String,
    var completed: Int,
    var schedule: String,
)