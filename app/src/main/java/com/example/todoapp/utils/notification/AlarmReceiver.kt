package com.example.todoapp.utils.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.todoapp.appComponent
import com.example.todoapp.model.TodoItem
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationUtils: NotificationUtils

    override fun onReceive(context: Context, intent: Intent) {

        // Выполнение действий при получении события оповещения
        val todoItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("todoItem", TodoItem::class.java)
        } else {
            intent.getSerializableExtra("todoItem")
        }
        val notificationComponent = context.appComponent.notificationComponent()
        notificationComponent.inject(this)

//        notificationUtils.showNotification("$notificationImportance IMPORTANCE",
//            notificationText?:"Здесь должен быть текст дела")
        notificationUtils.showNotification(todoItem as TodoItem)
    }
}