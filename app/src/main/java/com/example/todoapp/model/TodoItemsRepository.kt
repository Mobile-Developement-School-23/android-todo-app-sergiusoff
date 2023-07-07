package com.example.todoapp.model

import com.example.todoapp.retrofit.ApiEntity
import com.example.todoapp.retrofit.NetworkResult
import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {
    fun getAll(): Flow<List<TodoItem>>
    suspend fun add(item: TodoItem)
    suspend fun update(item: TodoItem)
    suspend fun delete(item: TodoItem)
    suspend fun clearAndInsertAllItems(items: List<TodoItem>)
    suspend fun getAllItemsFromBack(): NetworkResult<ApiEntity>
    suspend fun postAllItemsOnBack(newItems: List<TodoItem>): NetworkResult<ApiEntity>
    suspend fun getItemByIdFromBack(id: String): NetworkResult<ApiEntity>
    suspend fun postItemOnBack(newItem: TodoItem): NetworkResult<ApiEntity>
    suspend fun putItemOnBack(item: TodoItem): NetworkResult<ApiEntity>
    suspend fun deleteItemOnBack(id: String): NetworkResult<ApiEntity>
    suspend fun addItem(item: TodoItem): NetworkResult<ApiEntity>
    suspend fun updateItem(item: TodoItem): NetworkResult<ApiEntity>
    suspend fun deleteItem(item: TodoItem): NetworkResult<ApiEntity>
    suspend fun refreshRevision()
}