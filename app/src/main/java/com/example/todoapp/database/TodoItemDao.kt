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

/**
 * Интерфейс для доступа к данным сущности [TodoItem] с использованием Room Persistence Library.
 */
@Dao
interface TodoItemDao {

    /**
     * Получает все элементы списка задач.
     *
     * @return Flow с списком элементов [TodoItem].
     */
    @Query("SELECT * FROM TodoItems")
    fun getAll(): Flow<List<TodoItem>>

    /**
     * Получает элемент списка задач по его идентификатору.
     *
     * @param id Идентификатор элемента.
     * @return Элемент [TodoItem].
     */
    @Query("SELECT * FROM TodoItems WHERE id = :id")
    suspend fun get(id: UUID): TodoItem

    /**
     * Добавляет элемент списка задач.
     *
     * @param item Элемент для добавления.
     */
    @Insert(onConflict = REPLACE)
    suspend fun add(item: TodoItem)

    /**
     * Обновляет элемент списка задач.
     *
     * @param item Элемент для обновления.
     */
    @Update
    suspend fun update(item: TodoItem)

    /**
     * Удаляет элемент списка задач.
     *
     * @param item Элемент для удаления.
     */
    @Delete
    suspend fun delete(item: TodoItem)

    /**
     * Удаляет все элементы списка задач.
     */
    @Query("DELETE FROM TodoItems")
    suspend fun deleteAll()

    /**
     * Вставляет все элементы списка задач.
     *
     * @param items Список элементов для вставки.
     */
    @Insert(onConflict = REPLACE)
    suspend fun insertAll(items: List<TodoItem>)
}