package com.example.goosebuddy

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.goosebuddy.dao.*
import com.example.goosebuddy.models.*
import com.example.goosebuddy.models.calendar.CalendarItem
import com.example.goosebuddy.models.calendar.CalendarSeries

@Database(entities = [
    Routines::class,
    Subroutines::class,
    WeekdayData::class,
    Habits::class,
    UserData::class,
    Lock::class,
    CalendarItem::class,
    CalendarSeries::class
], version = 22)

abstract class AppDatabase : RoomDatabase() {
    abstract fun routinesDao(): RoutinesDao;
    abstract fun subroutinesDao(): SubroutinesDao;
    abstract fun weekdayDataDao(): WeekdayDataDao;
    abstract fun habitsDao(): HabitsDao;
    abstract fun userdataDao(): UserDataDao;
    abstract fun lockDao(): LockDao;
    abstract fun CalendarItemDao(): CalendarItemDao;
    abstract fun CalendarSeriesDao(): CalendarSeriesDao

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
