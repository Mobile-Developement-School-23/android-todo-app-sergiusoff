package com.example.todoapp.ioc

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.model.TodoItem
import com.example.todoapp.ui.stateholders.CreateEditViewModel
import com.example.todoapp.utils.notification.NotificationUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CreateEditViewModelFactory @AssistedInject constructor(
    @Assisted("todoItem") private val editedTodoItem: TodoItem,
    private val dataSynchronizer: DataSynchronizer,
    private val notificationUtils: NotificationUtils
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("UNCHECKED_CAST", modelClass::class.java.name)
        Log.d("UNCHECKED_CAST", editedTodoItem.text)
//        require(modelClass == CreateEditViewModel::class)
        return CreateEditViewModel(editedTodoItem, dataSynchronizer, notificationUtils) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("todoItem") editedTodoItem: TodoItem): CreateEditViewModelFactory
    }
}
