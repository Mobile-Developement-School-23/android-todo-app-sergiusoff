package com.example.todoapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.model.TodoItem
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TodoItemDao {

    @Query("SELECT * FROM TodoItems")
    fun getAll(): Flow<List<TodoItem>>

    @Query("SELECT * FROM TodoItems WHERE id = :id")
    suspend fun get(id: UUID): TodoItem

    @Insert(onConflict = REPLACE)
    suspend fun add(item: TodoItem)

    @Update
    suspend fun update(item: TodoItem)

    @Delete
    suspend fun delete(item: TodoItem)

    @Query("DELETE FROM TodoItems")
    suspend fun deleteAll()

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(items: List<TodoItem>)
}