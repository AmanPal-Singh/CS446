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
import com.example.goosebuddy.MainActivity
import com.example.goosebuddy.R


class HabitsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        println("ALARM RECEIVED IN")


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val intent = Intent(context, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val notif = NotificationCompat.Builder(context,"channelId")
                .setContentTitle("HONK HONK!")
                .setContentText("The day is almost up! Make sure to complete your remaining habits")
                .setSmallIcon(R.drawable.pencil)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(0, notif.build())

        }
    }
}
