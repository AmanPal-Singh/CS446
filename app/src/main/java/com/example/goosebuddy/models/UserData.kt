package com.example.goosebuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class UserData(
    @PrimaryKey(autoGenerate = true) val key: Int = 0,
    var name: String = "",
    val wat: Int = 0,
    var year: Int = 0,
    val hasRoommates: Boolean = false,
    val onStudentRes: Boolean = false,
    val firstTimeAlone: Boolean = false,
)
{
    override fun toString(): String {
        return "id: $key\n"+
                "wat: ${wat}\n" +
                "name: ${name}\n" +
                "year: ${year}\n" +
                "hasRoommates: ${hasRoommates}\n" +
                "onStudentRes: ${onStudentRes}\n" +
                "firstTimeAlone: ${firstTimeAlone}\n"
    }
}
