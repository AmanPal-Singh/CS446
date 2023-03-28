package com.example.goosebuddy

import RoutinesDao
import WeekdayDataDao
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.goosebuddy.models.WeekdayData
import com.example.goosebuddy.models.Routines


@Database(entities = [Routines::class, WeekdayData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routinesDao(): RoutinesDao;
    abstract fun weekdayDataDao(): WeekdayDataDao;
}

