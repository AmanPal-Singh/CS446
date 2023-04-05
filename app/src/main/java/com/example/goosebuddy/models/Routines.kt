package com.example.goosebuddy.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Routines (
    // Represents the model for routines
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
    var description: String,
    var completedSteps: Int,
    var totalSteps: Int
)

@Entity
data class Subroutines (
    @PrimaryKey(autoGenerate = true)
    val subId: Int,
    val routineId: Int,
    var title: String,
    var description: String,
    var completed: Boolean,
    var duration: Int
)

data class RoutineWithSubroutine (
    @Embedded
    val routines: Routines,
    @Relation(
        parentColumn = "id",
        entityColumn = "routineId"
    )
    val subroutines: List<Subroutines>
)