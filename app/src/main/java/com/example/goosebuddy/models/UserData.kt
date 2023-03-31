package com.example.goosebuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserData (
    // Represents the model for user collected in the onboarding process
    val name: String,
    val year: Int,
    val residence: Array<String>,
)