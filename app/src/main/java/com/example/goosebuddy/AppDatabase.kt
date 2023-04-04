package com.example.goosebuddy

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import com.example.goosebuddy.dao.*
import com.example.goosebuddy.models.*
import kotlinx.datetime.LocalDate

@Database(entities = [
    Routines::class,
    WeekdayData::class,
    Habits::class,
    UserData::class,
    Lock::class,
    CalendarItem::class
], version = 7)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routinesDao(): RoutinesDao;
    abstract fun weekdayDataDao(): WeekdayDataDao;
    abstract fun habitsDao(): HabitsDao;
    abstract fun userdataDao(): UserDataDao;
    abstract fun lockDao(): LockDao;
    abstract fun CalendarItemDao(): CalendarItemDao;

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
