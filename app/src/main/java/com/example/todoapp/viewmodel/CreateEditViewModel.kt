package com.example.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.locateLazy
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepository
import kotlinx.coroutines.launch

class CreateEditViewModel : ViewModel() {
    private val repository: TodoItemsRepository by locateLazy()

    fun saveTodoItem(todoItem: TodoItem) {
        viewModelScope.launch { repository.add(todoItem) }
    }

    fun updateTodoItem(todoItem: TodoItem) {
        viewModelScope.launch { repository.update(todoItem) }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch { repository.delete(todoItem) }
    }
}