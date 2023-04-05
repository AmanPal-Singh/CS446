package com.example.goosebuddy.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
class UserData (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String = "",
    var wat: Int = 0,
    var year: Int = 2000,
    var hasRoommates: Boolean = false,
    var onStudentRes: Boolean = false,
    var firstTimeAlone: Boolean = true){

    override fun toString(): String {
        return "id: $id\n"+
                "wat: ${wat}\n" +
                "name: ${name}\n" +
                "year: ${year}\n" +
                "hasRoommates: ${hasRoommates}\n" +
                "onStudentRes: ${onStudentRes}\n" +
                "firstTimeAlone: ${firstTimeAlone}\n"
    }
}
