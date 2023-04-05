package com.example.goosebuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.datetime.LocalDate
import java.sql.Date

@Entity
@TypeConverters(DateTimeConverters::class)
data class Habits (
    // Represents the model for habits
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
    var description: String,
    var completed: Int = 0, // unncessary now!
    var schedule: String = "Daily",
    var streak: Int = 0, // used to track how long a habit has been kept up!
    var completionSteps: Int = 4, // Steps to complete a habit
    var currentlyCompletedSteps: Int = 0, // Steps completed so far by a day
    var lastCompletedDate: LocalDate? = null,
)