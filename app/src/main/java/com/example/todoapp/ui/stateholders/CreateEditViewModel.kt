package com.example.todoapp.ui.stateholders

import android.util.Log
import androidx.lifecycle.ViewModel

import com.example.todoapp.model.TodoItem
import com.example.todoapp.ioc.DataSynchronizer
import com.example.todoapp.utils.notification.NotificationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.Date

/**
 * ViewModel для создания и редактирования элементов списка дел.
 * Основная задача этой ViewModel - сохранять, обновлять и удалять элементы списка дел через репозиторий.
 */
class CreateEditViewModel(
    private val todoItem: TodoItem,
    private val dataSynchronizer: DataSynchronizer,
    private val notificationUtils: NotificationUtils
) : ViewModel() {

    /**
     * Сохраняет элемент списка дел.
     * Действие будет выполняться в течение жизни ViewModel.
     * @param todoItem Элемент списка дел для сохранения.
     */
    fun saveTodoItem(todoItem: TodoItem) {
        Log.d("ADD_ITEM", todoItem.toString())
        setAlarmForTodoItem(todoItem)
        dataSynchronizer.saveTodoItem(todoItem)
    }

    /**
     * Обновляет элемент списка дел.
     * Действие будет выполняться в течение жизни ViewModel.
     * @param todoItem Элемент списка дел для обновления.
     */
    fun updateTodoItem() {
        Log.d("UPDATE_ITEM", todoItem.toString())
        setAlarmForTodoItem(todoItem)
        dataSynchronizer.updateTodoItem(todoItem)
    }

    /**
     * Удаляет элемент списка дел.
     * Действие будет выполняться в течение жизни ViewModel.
     * @param todoItem Элемент списка дел для удаления.
     */
    fun deleteTodoItem(todoItem: TodoItem) {
        Log.d("UPDATE_ITEM", todoItem.toString())
        dataSynchronizer.deleteTodoItem(todoItem)
    }

    private fun setAlarmForTodoItem(item: TodoItem) {
        val timeInMillis = item.deadline?.time ?: return
//        Log.d("NotificationTime", ((timeInMillis - System.currentTimeMillis()) / 1000).toString())
        notificationUtils.setAlarm(timeInMillis, todoItem)
    }
}