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
import com.example.todoapp.model.TodoItem
import com.example.todoapp.ui.view.CreateEditFragment
import com.example.todoapp.ui.view.MainActivity
import javax.inject.Inject


const val channelID = "ChannelID1"
const val groupID = "GroupID1"
const val group_name = "GroupName"
const val channelName = "ChannelName"
const val channelDesc = "ChannelAboutDeadlines"

@AppScope
class NotificationUtils @Inject constructor(private val context: Context) {
    // Функция для создания уведомления
    fun showNotification(todoItem: TodoItem) {
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
                .setContentTitle("${todoItem.importance} IMPORTANCE")
                .setContentText(todoItem.text)
                .setChannelId(channelID)
                .setContentIntent(createOpenPendingIntent(todoItem))
                .addAction(R.drawable.down_arrow, "Отложить на сутки", createPostponePendingIntent(todoItem))
            notificationManager.notify(0, notificationBuilder.build())
        }
    }

    fun setAlarm(timeInMillis: Long, todoItem: TodoItem) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("todoItem", todoItem)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }

    private fun createPostponePendingIntent(todoItem: TodoItem): PendingIntent {
        val intent = Intent(context, PostponeReceiver::class.java)
        intent.putExtra("todoItem", todoItem)
        return PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createOpenPendingIntent(todoItem: TodoItem): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("todoItem", todoItem)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}