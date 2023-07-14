package com.example.todoapp.utils.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.todoapp.appComponent
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationUtils: NotificationUtils

    override fun onReceive(context: Context, intent: Intent) {

        // Выполнение действий при получении события оповещения
        val notificationText = intent.getStringExtra("notification_text")
        val notificationImportance = intent.getStringExtra("notification_importance")
        val notificationComponent = context.appComponent.notificationComponent()
        notificationComponent.inject(this)

        notificationUtils.showNotification("$notificationImportance IMPORTANCE",
            notificationText?:"Здесь должен быть текст дела")
    }
}