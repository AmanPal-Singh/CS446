package com.example.goosebuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
class UserData {
    @PrimaryKey var name: String = ""
    var year: Int = 2000
    var hasRoommates: Boolean = false
    var onStudentRes: Boolean = false
    var firstTimeAlone: Boolean = true

    override fun toString(): String {
        return "name: ${name}\n" +
                "year: ${year}\n" +
                "hasRoommates: ${hasRoommates}\n" +
                "onStudentRes: ${onStudentRes}\n" +
                "firstTimeAlone: ${firstTimeAlone}\n"
    }
}
