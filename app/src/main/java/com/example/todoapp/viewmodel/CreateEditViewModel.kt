package com.example.todoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.App
import com.example.todoapp.locateLazy
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateEditViewModel : ViewModel() {
    private val _todoItem = MutableStateFlow<TodoItem?>(null)
    val todoItem: StateFlow<TodoItem?> get() = _todoItem
    private val repository: TodoItemsRepository by locateLazy()

    fun loadTodoItem(myInt: Int) {
        viewModelScope.launch {
            val todoItem = repository.getTodoItem(myInt)
            _todoItem.value = todoItem
        }
    }

    fun saveTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.addTodoItem(todoItem)
        }
    }

    fun deleteTodoItem() {
        val currentItem = _todoItem.value
        if (currentItem != null) {
            viewModelScope.launch {
                repository.deleteTodoItem(currentItem)
                _todoItem.value = null
            }
        }
    }
}