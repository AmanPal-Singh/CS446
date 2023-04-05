package com.example.goosebuddy.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class HabitsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        println("ALARM RECEIVED IN")
    }
}