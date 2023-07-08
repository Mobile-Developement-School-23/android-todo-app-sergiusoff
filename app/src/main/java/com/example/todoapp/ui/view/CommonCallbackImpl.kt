package com.example.todoapp.ui.view

import androidx.recyclerview.widget.DiffUtil

/**
 * Реализация коллбэка для DiffUtil, используемого для сравнения двух списков элементов,
 * которую я так (пока) и не добавил в работу
 * @param oldItems Список старых элементов.
 * @param newItems Список новых элементов.
 */
class CommonCallbackImpl<T>(
    private val oldItems: List<T>,
    private val newItems: List<T>
): DiffUtil.Callback() {

    /**
     * Возвращает размер старого списка элементов.
     * @return Размер старого списка.
     */
    override fun getOldListSize() = oldItems.size

    /**
     * Возвращает размер нового списка элементов.
     * @return Размер нового списка.
     */
    override fun getNewListSize() = newItems.size

    /**
     * Реализация функции сравнения элементов списка.
     */
    private val areItemsTheSameImpl: (oldItem: T, newItem: T) -> Boolean =
        { oldItem, newItem -> oldItem == newItem }

    /**
     * Проверяет, являются ли элементы на указанных позициях одним и тем же элементом.
     * @param oldItemPosition Позиция элемента в старом списке.
     * @param newItemPosition Позиция элемента в новом списке.
     * @return `true`, если элементы одинаковые, иначе `false`.
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return areItemsTheSameImpl(oldItem, newItem)
    }

    /**
     * Реализация функции сравнения содержимого элементов списка.
     */
    private val areContentsTheSameImpl: (oldItem: T, newItem: T) -> Boolean =
        { oldItem, newItem -> oldItem == newItem }

    /**
     * Проверяет, являются ли содержимое элементов на указанных позициях одинаковым.
     * @param oldItemPosition Позиция элемента в старом списке.
     * @param newItemPosition Позиция элемента в новом списке.
     * @return `true`, если содержимое элементов одинаковое, иначе `false`.
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return areContentsTheSameImpl(oldItem, newItem)
    }
}