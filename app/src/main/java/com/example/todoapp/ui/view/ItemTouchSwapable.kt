package com.example.todoapp.ui.view

/**
 * Интерфейс, определяющий возможность перемещения и проверки элементов списка с помощью смахивания.
 * Данный интерфейс реализуется классом TodoItemAdapter.kt
 */
interface ItemTouchSwapable {
    /**
     * Вызывается при смахивании элемента списка.
     * @param position Позиция элемента списка.
     */
    fun onItemDismiss(position: Int)
    /**
     * Вызывается при изменении состояния флажка элемента списка.
     * @param position Позиция элемента списка.
     */
    fun onItemChecked(position: Int)
}
