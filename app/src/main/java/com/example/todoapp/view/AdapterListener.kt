package com.example.todoapp.view

import com.example.todoapp.model.TodoItem

/**
 * Интерфейс слушателя адаптера, который определяет действия, связанные с элементами списка.
 * Данный интерфейс реализуется классом TodoListFragment.kt
 */
interface AdapterListener {

    /**
     * Вызывается при удалении элемента списка.
     * @param item Удаляемый элемент списка.
     */
    fun onTodoItemDeleted(item: TodoItem)

    /**
     * Вызывается при изменении состояния флажка элемента списка.
     * @param position Позиция элемента списка.
     */
    fun onTodoItemChecked(position: Int)

    /**
     * Вызывается при нажатии на кнопку редактирования элемента списка.
     * @param position Позиция элемента списка.
     */
    fun onEditClicked(position: Int)
}