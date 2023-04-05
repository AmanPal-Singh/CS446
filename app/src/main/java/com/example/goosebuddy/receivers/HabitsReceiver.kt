package com.example.goosebuddy.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class HabitsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        println("ALARM RECEIVED IN")
    }
}
