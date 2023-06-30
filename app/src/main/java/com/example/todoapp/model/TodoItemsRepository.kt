package com.example.todoapp.model

import android.util.Log
import com.example.todoapp.database.TodoItemsDatabase
import com.example.todoapp.retrofit.ApiEntity
import com.example.todoapp.retrofit.NetworkResult
import com.example.todoapp.retrofit.TodoItemsApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Репозиторий для управления списком задач [TodoItem].
 */
class TodoItemsRepository(
    private val db: TodoItemsDatabase,
    private val todoItemsApiService: TodoItemsApiService
) {

    var revision: Int = 21
    private val dao get() = db.itemsDao

    fun getAll(): Flow<List<TodoItem>> = dao.getAll()

    suspend fun add(item: TodoItem) = dao.add(item)

    suspend fun update(item: TodoItem) = dao.update(item)

    suspend fun delete(item: TodoItem) = dao.delete(item)

    suspend fun clearAndInsertAllItems(items: List<TodoItem>) {
        dao.deleteAll()
        dao.insertAll(items)
    }

    suspend inline fun executeNetworkRequest(crossinline apiCall: suspend () -> ApiEntity): NetworkResult<ApiEntity> {
        return try {
            val response = apiCall()
            revision = response.revision!!
            NetworkResult.Success(response)

        } catch (e: Exception) {
            val errorMessage = "Ошибка при выполнении сетевого запроса: ${e.message}"
            refreshRevision()
            NetworkResult.Error(errorMessage, e)
        }
    }

    suspend fun getAllItemsFromBack(): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.getTodoItems() }

    suspend fun updateAllItemsOnBack(newItems: ApiEntity): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.updateTodoItems(revision, newItems) }

    suspend fun getItemByIdFromBack(id: String): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.getTodoItem(id) }

    suspend fun postItemOnBack(newItem: TodoItem): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.postTodoItem(revision, ApiEntity(null, newItem, null, null)) }

    suspend fun putItemOnBack(item: TodoItem): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.putTodoItem(revision, item.id.toString(), ApiEntity(null, item, null, null)) }

    suspend fun deleteItemOnBack(id: String): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.deleteTodoItem(revision, id) }

    suspend fun addItem(item: TodoItem): NetworkResult<ApiEntity> {
        add(item)
        return postItemOnBack(item)
    }

    suspend fun updateItem(item: TodoItem): NetworkResult<ApiEntity> {
        update(item)
        return putItemOnBack(item)
    }

    suspend fun deleteItem(item: TodoItem): NetworkResult<ApiEntity> {
        delete(item)
        return deleteItemOnBack(item.id.toString())
    }
    suspend fun refreshRevision() {
        CoroutineScope(Dispatchers.IO).launch {
            revision = when (val result = getAllItemsFromBack()) {
                is NetworkResult.Success -> result.data.revision!!
                else -> -1
            }
        }
    }
}
