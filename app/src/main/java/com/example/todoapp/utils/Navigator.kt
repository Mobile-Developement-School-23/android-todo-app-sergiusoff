package com.example.todoapp.utils

import androidx.fragment.app.Fragment
import com.example.todoapp.model.TodoItem
import java.util.UUID

/**
 * Возвращает экземпляр навигатора, связанного с активностью фрагмента.
 * Релизует этот интерфейс MainActivity
 * @return Экземпляр навигатора.
 */
fun Fragment.navigator(): Navigator{
    return requireActivity() as Navigator
}
/**
 * Интерфейс навигатора, предоставляющий методы для навигации и управления задачами.
 */
interface Navigator {
    /**
     * Отображает детали задачи по указанному индексу.
     * @param n Индекс задачи.
     */
    fun showDetails(todoItem: TodoItem?)
    /**
     * Отображает список задач.
     */
    fun showList()
}