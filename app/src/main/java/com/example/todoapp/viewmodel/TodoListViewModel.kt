package com.example.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.App
import com.example.todoapp.model.TodoItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel, отвечающая за управление списком задач.
 */
class TodoListViewModel : ViewModel() {
    private val todoItemsRepository = App.todoItemsRepository
    private val _todoItems = MutableStateFlow(emptyList<TodoItem>())
    val todoItems: Flow<List<TodoItem>> get() = _todoItems

    /**
     * Загружает список задач.
     */
    fun loadTodoItems(){
        todoItemsRepository.getTodoItems()
            .onEach { _todoItems.value = it }
            .launchIn(viewModelScope)
    }

    /**
     * Добавляет задачу в список.
     * @param todoItem Задача для добавления.
     * @return Индекс добавленной задачи.
     */
    fun addTodoItem(todoItem: TodoItem): Int{
        todoItemsRepository.addTodoItem(todoItem)
        return 0
    }

    /**
     * Возвращает количество отмеченных задач.
     * @return Количество отмеченных задач.
     */
    fun getCheckedItemCount(): Int{
        return todoItemsRepository.getCheckedItemCount()
    }

    /**
     * Удаляет задачу из списка.
     * @param item Задача для удаления.
     */
    fun deleteTodoItem(item: TodoItem) {
        todoItemsRepository.deleteTodoItem(item)
    }

    /**
     * Отмечает задачу как выполненную.
     * @param position Позиция задачи в списке.
     */
    fun checkTodoItem(position: Int) {
        todoItemsRepository.checkTodoItem(position)
    }

    /**
     * Возвращает общее количество задач.
     * @return Общее количество задач.
     */
    fun getTodosCount(): Int = todoItemsRepository.getTodosCount()
}