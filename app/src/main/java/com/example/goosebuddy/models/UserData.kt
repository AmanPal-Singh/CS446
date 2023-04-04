package com.example.goosebuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class UserData(
    @PrimaryKey val key: Int = 0,
    var name: String = "",
    val wat: Int = 0,
    val year: Int = 0,
    val hasRoommates: Boolean = false,
    val onStudentRes: Boolean = false,
    val firstTimeAlone: Boolean = true,
)
{
    override fun toString(): String {
        return "name: ${name}\n" +
                "wat: ${wat}\n" +
                "year: ${year}\n" +
                "hasRoommates: ${hasRoommates}\n" +
                "onStudentRes: ${onStudentRes}\n" +
                "firstTimeAlone: ${firstTimeAlone}\n"
    }
}
