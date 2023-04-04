package com.example.goosebuddy

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.goosebuddy.dao.*
import com.example.goosebuddy.models.*


@Database(entities = [Routines::class, WeekdayData::class, Habits::class, UserData::class, Lock::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routinesDao(): RoutinesDao;
    abstract fun weekdayDataDao(): WeekdayDataDao;
    abstract fun habitsDao(): HabitsDao;
    abstract fun userdataDao(): UserDataDao;
    abstract fun lockDao(): LockDao;

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

