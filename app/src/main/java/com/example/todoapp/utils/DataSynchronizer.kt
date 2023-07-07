package com.example.todoapp.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.di.AppScope
import com.example.todoapp.model.Event
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepository
import com.example.todoapp.model.TodoItemsRepositoryImpl
import com.example.todoapp.retrofit.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AppScope
class DataSynchronizer @Inject constructor(
    private val todoItemsRepository: TodoItemsRepository
){
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val _todoItemProcess = MutableLiveData<Event<String>>()
    val todoItemProcess: LiveData<Event<String>> = _todoItemProcess

    /**
     * Устанавливает результат обработки операции с элементом списка дел.
     * @param result Результат операции в виде события.
     */
    fun setTodoItemProcess(result: Event<String>) {
        _todoItemProcess.postValue(result)
    }

    /**
     * Выполняет операцию сохранения элемента списка дел.
     * @param item Элемент списка дел для сохранения.
     */
    fun saveTodoItem(item: TodoItem) {
        coroutineScope.launch {
            todoItemsRepository.addItem(item)
            // Другие необходимые операции сохранения
        }
    }

    /**
     * Выполняет операцию изменения элемента списка дел.
     * @param item Элемент списка дел для изменения.
     */
    fun updateTodoItem(item: TodoItem) {
        coroutineScope.launch {
            when (val result = todoItemsRepository.updateItem(item)){
                is NetworkResult.Success -> setTodoItemProcess(Event("Данные успешно обновлены"))
                is NetworkResult.Error -> setTodoItemProcess(Event("Не удалось обновить данные: " + result.errorMessage))
            }
        }
    }

    /**
     * Выполняет операцию удаления элемента списка дел.
     * @param item Элемент списка дел для удаления.
     */
    fun deleteTodoItem(item: TodoItem) {
        coroutineScope.launch {
            todoItemsRepository.deleteItem(item)
            // Другие необходимые операции удаления
        }
    }
}