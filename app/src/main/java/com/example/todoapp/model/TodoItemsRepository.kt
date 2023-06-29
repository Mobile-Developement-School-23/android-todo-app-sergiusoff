package com.example.todoapp.model

import com.example.todoapp.database.TodoItemsDatabase
import com.example.todoapp.retrofit.ApiEntity
import com.example.todoapp.retrofit.TodoItemsApiService
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий для управления списком задач [TodoItem].
 */
class TodoItemsRepository(
    private val db: TodoItemsDatabase,
    private val todoItemsApiService: TodoItemsApiService
) {

    private val dao get() = db.itemsDao

    fun getAll(): Flow<List<TodoItem>> = dao.getAll()

    suspend fun add(item: TodoItem) = dao.add(item)

    suspend fun update(item: TodoItem) = dao.update(item)

    suspend fun delete(item: TodoItem) = dao.delete(item)

    suspend fun getAllItemsFromBack() = todoItemsApiService.getTodoItems()

    suspend fun updateAllItemsOnBack(revision: Int, newItems: ApiEntity) = todoItemsApiService.updateTodoItems(revision, newItems)

    suspend fun getItemByIdFromBack(id: String) = todoItemsApiService.getTodoItem(id)

    suspend fun postItemOnBack(revision: Int, newItem: ApiEntity) = todoItemsApiService.postTodoItem(revision, newItem)

    suspend fun putItemOnBack(revision: Int, id: String, newItem: ApiEntity) = todoItemsApiService.putTodoItem(revision, id, newItem)

    suspend fun deleteItemOnBack(revision: Int, id: String) = todoItemsApiService.deleteTodoItem(revision, id)
}
