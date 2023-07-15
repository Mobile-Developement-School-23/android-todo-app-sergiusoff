package com.example.todoapp.ui.stateholders

import android.util.Log
import androidx.lifecycle.ViewModel

import com.example.todoapp.model.TodoItem
import com.example.todoapp.ioc.DataSynchronizer
import com.example.todoapp.model.Importance
import com.example.todoapp.ui.view.compose.manage.CreateEditScreenEvent
import com.example.todoapp.utils.notification.NotificationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

/**
 * ViewModel для создания и редактирования элементов списка дел.
 * Основная задача этой ViewModel - сохранять, обновлять и удалять элементы списка дел через репозиторий.
 */
class CreateEditViewModel(
    private val tempItem: TodoItem,
    private val dataSynchronizer: DataSynchronizer,
    private val notificationUtils: NotificationUtils
) : ViewModel() {

    private val _todoItem: MutableStateFlow<TodoItem> = MutableStateFlow(tempItem)
    val todoItem = _todoItem.asStateFlow()

    /**
     * Сохраняет элемент списка дел.
     * Действие будет выполняться в течение жизни ViewModel.
     * @param todoItem Элемент списка дел для сохранения.
     */
    fun saveTodoItem(todoItem: TodoItem) {
        if (todoItem.text != "") {
            Log.d("ADD_ITEM", todoItem.toString())
            setAlarmForTodoItem(todoItem)
        }
        dataSynchronizer.saveTodoItem(todoItem)
    }

    /**
     * Обновляет элемент списка дел.
     * Действие будет выполняться в течение жизни ViewModel.
     * @param todoItem Элемент списка дел для обновления.
     */
    fun updateTodoItem(todoItem: TodoItem) {
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
        notificationUtils.setAlarm(timeInMillis, todoItem.value)
    }

    private fun changeTitle(text: String) {
        _todoItem.update { _todoItem.value.copy(text = text) }
    }

    private fun changeImportance(important: Importance) {
        _todoItem.update { _todoItem.value.copy(importance = important) }
    }

    private fun changeDeadline(deadline: Date?) {
        _todoItem.update { _todoItem.value.copy(deadline = deadline) }
    }
    fun receiveEvent(event: CreateEditScreenEvent) {
        when (event) {
            is CreateEditScreenEvent.ChangeText -> changeTitle(event.text)
            is CreateEditScreenEvent.ChangeImportance -> changeImportance(event.importance)
            is CreateEditScreenEvent.ChangeDeadline -> changeDeadline(event.deadline)
            CreateEditScreenEvent.ClearDeadline -> changeDeadline(null)
            CreateEditScreenEvent.CreateTodoItem -> saveTodoItem(_todoItem.value)
            CreateEditScreenEvent.SaveTodoItem -> updateTodoItem(_todoItem.value)
            CreateEditScreenEvent.DeleteTodoItem -> deleteTodoItem(_todoItem.value)
        }
    }
}