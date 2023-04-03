package com.example.goosebuddy

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.goosebuddy.dao.HabitsDao
import com.example.goosebuddy.dao.RoutinesDao
import com.example.goosebuddy.dao.WeekdayDataDao
import com.example.goosebuddy.models.Habits
import com.example.goosebuddy.models.Routines
import com.example.goosebuddy.models.WeekdayData



@Database(entities = [Routines::class, WeekdayData::class, Habits::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routinesDao(): RoutinesDao;
    abstract fun weekdayDataDao(): WeekdayDataDao;
    abstract fun habitsDao(): HabitsDao;

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun createInstance(context: Context): AppDatabase {
            if(instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "database-name"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build();
            }

            return instance!!

        }}
}

