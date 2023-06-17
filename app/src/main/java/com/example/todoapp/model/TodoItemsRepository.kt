package com.example.todoapp.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

/**
 * Репозиторий для управления списком задач [TodoItem].
 *
 * @property todoItems Список задач.
 * @property todoItemsFlow Поток, содержащий список задач.
 */
class TodoItemsRepository {
    private val todoItems = mutableListOf<TodoItem>()
    private val todoItemsFlow = MutableStateFlow(todoItems)

    /**
     * Инициализирует репозиторий TodoItemsRepository.
     */
    init {
        // Добавление нескольких элементов TodoItem в список при инициализации объекта
        // для примера и тестирования функциональности.
        addTodoItem(TodoItem("-1", "1тест", Importance.NORMAL, Date(115, 12, 15), false, Date(115, 12, 12), Date(117, 12, 31)))
        addTodoItem(TodoItem("-1", "2тесттесттесттесттесттесттест", Importance.LOW, null, true, Date(118, 1, 1), null))
        addTodoItem(TodoItem("-1", "3тесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттеттесттесттетесттесттест", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "4тесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттеттесттесттетесттесттест", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "5тесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттеттесттесттетесттесттест", Importance.HIGH, Date(118, 12, 31), false, Date(2017, 12, 31), null))
        addTodoItem(TodoItem("-1", "6тесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттеттесттесттетесттесттест", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "7тесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттеттесттесттетесттесттест", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "8тесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттеттесттесттетесттесттест", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "9тесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттеттесттесттетесттесттест", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "10тесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттеттесттесттетесттесттест", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "11тесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттеттесттесттетесттесттест", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "12тесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттеттесттесттетесттесттест", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "13тесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттеттесттесттетесттесттест", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "14тесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттеттесттесттетесттесттест", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "15тесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттеттесттесттетесттесттест", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "16тесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттеттесттесттетесттесттест", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "17sf", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "18тесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттесттеттесттесттетесттесттест", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "19fs", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "20fs", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "21fs", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "22fs", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "23fs", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "24fs", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "25fs", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "26fs", Importance.HIGH, null, false, Date(117, 12, 31), null))
        addTodoItem(TodoItem("-1", "1тест", Importance.NORMAL, null, true, Date(117, 12, 12), null))
        addTodoItem(TodoItem("-1", "2тесттесттесттесттесттесттест", Importance.LOW, null, false, Date(117, 1, 1), null))
    }

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
    fun getTodoItem(position: Int): TodoItem{
        return todoItems[position]
    }

    /**
     * Добавляет новый элемент TodoItem в список задач или обновляет существующий элемент с указанным ID.
     *
     * @param todoItem Элемент TodoItem для добавления или обновления.
     */
    fun addTodoItem(todoItem: TodoItem) {
        // Если у задачи нет ID ("-1"), генерируем и присваиваем ей новый ID
        if (todoItem.id == "-1") {
            todoItem.id = todoItems.size.toString()
            todoItems.add(todoItem)
        }
        else {
            // Если у задачи уже есть ID, обновляем задачу в списке по соответствующему ID
            todoItems[todoItem.id.toInt()] = todoItem
        }
        // Обновляем значение потока (дальше также)
        todoItemsFlow.value = todoItems.toMutableList()
    }


    /**
     * Удаляет указанный элемент TodoItem из списка задач.
     *
     * @param item Элемент TodoItem для удаления.
     */
    fun deleteTodoItem(item: TodoItem) {
        todoItems.remove(item)
        todoItemsFlow.value = todoItems.toMutableList()
    }

    /**
     * Изменяет состояние поля isDone (выполнения задачи) по указанной позиции в списке задач.
     *
     * @param position Позиция элемента в списке задач.
     */
    fun checkTodoItem(position: Int) {
        // Инвертируем флаг выполнения задачи
        todoItems[position].isDone = !todoItems[position].isDone
        todoItemsFlow.value = todoItems.toMutableList()
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
        todoItemsFlow.value = todoItems.toMutableList()
    }

    /**
     * Возвращает общее количество задач в списке.
     *
     * @return Общее количество задач.
     */
    fun getTodosCount(): Int = todoItems.size
}