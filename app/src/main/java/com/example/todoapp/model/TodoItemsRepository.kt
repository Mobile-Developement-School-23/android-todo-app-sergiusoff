package com.example.todoapp.model

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.todoapp.model.database.TodoItemsDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

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

    suspend fun getTodoItem(myUUID: UUID): TodoItem = dao.get(myUUID)

    suspend fun delete(item: TodoItem) = dao.delete(item)

    private val todoItems = mutableListOf<TodoItem>()
    private val todoItemsFlow = MutableStateFlow(todoItems)

    /**
     * Возвращает поток, содержащий список задач.
     *
     * @return Поток, содержащий список задач.
     */
    fun getTodoItems(): Flow<List<TodoItem>> {
        return todoItemsFlow
    }

    /**
     * Возвращает элемент TodoItem по указанной позиции в списке задач.
     *
     * @param position Позиция элемента в списке задач.
     * @return Элемент TodoItem.
     */


    /**
     * Удаляет указанный элемент TodoItem из списка задач.
     *
     * @param item Элемент TodoItem для удаления.
     */
    fun deleteTodoItem(item: TodoItem) {
        todoItems.remove(item)
//        todoItemsFlow.value = todoItems.toMutableList()
    }

    /**
     * Изменяет состояние поля isDone (выполнения задачи) по указанной позиции в списке задач.
     *
     * @param position Позиция элемента в списке задач.
     */
    fun checkTodoItem(position: Int) {
        // Инвертируем флаг выполнения задачи
        todoItems[position].isDone = !todoItems[position].isDone
//        todoItemsFlow.value = todoItems.toMutableList()
    }

    /**
     * Возвращает количество выполненных задач в списке.
     *
     * @return Количество выполненных задач.
     */
    fun getCheckedItemCount(): Int {
        // Подсчитываем количество выполненных задач в списке
        return todoItems.filter { it.isDone }.size
    }

    /**
     * Удаляет элемент TodoItem из списка задач по указанному индексу.
     *
     * @param ind Индекс элемента для удаления.
     */
    fun deleteTodoItemByInd(ind: Int) {
        todoItems.removeAt(ind)
//        todoItemsFlow.value = todoItems.toMutableList()
    }

    /**
     * Возвращает общее количество задач в списке.
     *
     * @return Общее количество задач.
     */
    fun getTodosCount(): Int = todoItems.size
}