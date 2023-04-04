package com.example.goosebuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
class UserData {
    @PrimaryKey var name: String = ""
    var wat: Int = 0
    var year: Int = 0
    var hasRoommates: Boolean = false
    var onStudentRes: Boolean = false
    var firstTimeAlone: Boolean = true

    override fun toString(): String {
        return "name: ${name}\n" +
                "wat: ${wat}\n" +
                "year: ${year}\n" +
                "hasRoommates: ${hasRoommates}\n" +
                "onStudentRes: ${onStudentRes}\n" +
                "firstTimeAlone: ${firstTimeAlone}\n"
    }
}
