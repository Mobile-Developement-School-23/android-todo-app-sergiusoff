package com.example.todoapp.utils.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.todoapp.R
import com.example.todoapp.di.AppScope
import com.example.todoapp.model.Importance
import javax.inject.Inject

const val notificationID = 1
const val channelID = "channel1"
const val groupID = "channel1"
const val group_name = "groooooup"
const val channelName = "MFC (name)"
const val channelDesc = "my first channel (desc)"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

@AppScope
class NotificationUtils @Inject constructor(private val context: Context) {
    // Функция для создания уведомления
    fun showNotification(title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannelGroup(
                NotificationChannelGroup(groupID, group_name))

            val channel = NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH,
            )

            channel.apply {
                description = channelDesc
                enableVibration(true)
                group = groupID
            }

            notificationManager.createNotificationChannel(channel)

            val notificationBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setChannelId(channelID)

            notificationManager.notify(0, notificationBuilder.build())
        }
    }

    // Функция для установки оповещения
    fun setAlarm(timeInMillis: Long, notificationText: String, notificationImportance: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("notification_text", notificationText)
        intent.putExtra("notification_importance", notificationImportance)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }
}