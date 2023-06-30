package com.example.todoapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.locateLazy
import com.example.todoapp.model.Event
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepository
import com.example.todoapp.retrofit.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateEditViewModel : ViewModel() {
    private val repository: TodoItemsRepository by locateLazy()


    private val viewModelScope = CoroutineScope(Dispatchers.IO)
    lateinit var sharedViewModel: SharedViewModel

    fun saveTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            when (val result = repository.addItem(todoItem)){
                is NetworkResult.Success -> sharedViewModel.setTodoItemProcess(Event("Данные успешно сохранены"))
                is NetworkResult.Error -> sharedViewModel.setTodoItemProcess(Event("Не удалось сохранить данные:" + result.errorMessage))
            }
        }
    }

    fun updateTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            when (val result = repository.updateItem(todoItem)){
                is NetworkResult.Success -> sharedViewModel.setTodoItemProcess(Event("Данные успешно обновлены"))
                is NetworkResult.Error -> sharedViewModel.setTodoItemProcess(Event("Не удалось обновить данные:" + result.errorMessage))
            }
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            when (val result = repository.deleteItem(todoItem)){
                is NetworkResult.Success -> sharedViewModel.setTodoItemProcess(Event("Данные успешно удалены"))
                is NetworkResult.Error -> sharedViewModel.setTodoItemProcess(Event("Не удалось удалить данные:" + result.errorMessage))
            }
        }
    }

//    fun updateTodoItem(todoItem: TodoItem) {
//        viewModelScope.launch { repository.updateItem(todoItem)}
//    }
//    fun deleteTodoItem(todoItem: TodoItem) {
//        viewModelScope.launch { repository.delete(todoItem) }
//    }
}