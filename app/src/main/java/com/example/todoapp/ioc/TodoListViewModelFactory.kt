package com.example.todoapp.ioc

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.model.TodoItemsRepository
import com.example.todoapp.ui.stateholders.TodoListViewModel
import com.example.todoapp.utils.notification.NotificationUtils


import javax.inject.Inject

class TodoListViewModelFactory @Inject constructor(
    private val todoItemsRepository: TodoItemsRepository,
    private val notificationUtils: NotificationUtils
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("UNCHECKED_CAST", modelClass::class.java.name)
//        require(modelClass == TodoListViewModel::class)
        return TodoListViewModel(todoItemsRepository, notificationUtils) as T
    }
}