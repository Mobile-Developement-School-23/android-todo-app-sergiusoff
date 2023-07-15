package com.example.todoapp.utils.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.todoapp.appComponent
import com.example.todoapp.ioc.DataSynchronizer
import com.example.todoapp.model.TodoItem

import java.util.Calendar
import javax.inject.Inject

class PostponeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var dateSynchronizer: DataSynchronizer

    override fun onReceive(context: Context, intent: Intent) {
        val notificationComponent = context.appComponent.notificationComponent()
        notificationComponent.inject(this)
        val todoItem = intent.getSerializableExtra("todoItem") as? TodoItem
        Log.d("PostponeReceiver", "onReceive")
        if (todoItem != null) {
            val newDeadline = todoItem.deadline?.let {
                val calendar = Calendar.getInstance()
                calendar.time = it
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                calendar.time
            }
            todoItem.deadline = newDeadline
            Log.d("PostponeReceiver", newDeadline.toString())
            dateSynchronizer.updateTodoItem(todoItem)
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(0)
    }
}