package com.example.goosebuddy

import com.example.goosebuddy.dao.RoutinesDao
import com.example.goosebuddy.dao.WeekdayDataDao
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.goosebuddy.dao.HabitsDao
import com.example.goosebuddy.models.WeekdayData
import com.example.goosebuddy.models.Routines
import com.example.goosebuddy.models.Habits



@Database(entities = [Routines::class, WeekdayData::class, Habits::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routinesDao(): RoutinesDao;
    abstract fun weekdayDataDao(): WeekdayDataDao;
    abstract fun habitsDao(): HabitsDao;
}

