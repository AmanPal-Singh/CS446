package com.example.goosebuddy.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.goosebuddy.AppDatabase
import com.example.goosebuddy.MainActivity
import com.example.goosebuddy.R
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now


class ResetHabitsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        println("ALARM RECEIVED IN RESETHABITS")
        var db = AppDatabase.createInstance(context)
        var habitsDao = db.habitsDao()
        var habits = habitsDao.getAll()
        println("${habits}")

        habits.forEach { item ->
            item.currentlyCompletedSteps = 0
        }

        habitsDao.update(habits = habits.toTypedArray())

    }
}
