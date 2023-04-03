package com.example.goosebuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Lock (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var value: Int,
)