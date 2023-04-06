package com.example.goosebuddy.models

import androidx.room.*

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

@Entity (
    foreignKeys = [
        ForeignKey(
        entity = Routines::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("routineId"),
        onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Subroutines (
    @PrimaryKey(autoGenerate = true)
    val subId: Int,
    @ColumnInfo(index = true)
    val routineId: Int,
    var title: String,
    var description: String,
    var completed: Boolean,
    var duration: Int
)

data class RoutineWithSubroutine (
    @Embedded
    var routines: Routines,
    @Relation(
        parentColumn = "id",
        entityColumn = "routineId"
    )
    var subroutines: List<Subroutines>
)