package com.example.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.locateLazy
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel, отвечающая за управление списком задач.
 */
class TodoListViewModel : ViewModel() {
    private val todoItemsRepository: TodoItemsRepository by locateLazy()
    val todoItems = todoItemsRepository.getAll().asLiveDataFlow()
    private fun <T> Flow<T>.asLiveDataFlow() = shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

    fun updateTodoItem(item: TodoItem) {
        viewModelScope.launch { todoItemsRepository.update(item) }
    }

    fun deleteTodoItem(item: TodoItem) {
        viewModelScope.launch { todoItemsRepository.delete(item) }
    }
}
