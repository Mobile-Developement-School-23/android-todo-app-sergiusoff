package com.example.todoapp.model

import com.example.todoapp.database.TodoItemsDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Репозиторий для управления списком задач [TodoItem].
 *
 * @property todoItems Список задач.
 * @property todoItemsFlow Поток, содержащий список задач.
 */
class TodoItemsRepository(
    private val db: TodoItemsDatabase
) {

    private val dao get() = db.itemsDao

    fun getAll(): Flow<List<TodoItem>> = dao.getAll()

    suspend fun add(item: TodoItem) = dao.add(item)

    suspend fun update(item: TodoItem) = dao.update(item)

    suspend fun delete(item: TodoItem) = dao.delete(item)

    private val todoItems = mutableListOf<TodoItem>()
    private val todoItemsFlow = MutableStateFlow(todoItems)

}