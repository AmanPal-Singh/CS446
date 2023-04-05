package com.example.goosebuddy.models

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class DateTimeConverters {
    @TypeConverter
    fun toLocalDate(value: Int?): LocalDate? {
        return value?.let { LocalDate.fromEpochDays(value.toInt()) }
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Int? {
        return date?.toEpochDays()
    }

    @TypeConverter
    fun toLocalTime(value: Int?): LocalTime? {
        return value?.let { LocalTime.fromSecondOfDay(value) }
    }

    @TypeConverter
    fun fromLocalTime(date: LocalTime?): Int? {
        return date?.toSecondOfDay()
    }
}