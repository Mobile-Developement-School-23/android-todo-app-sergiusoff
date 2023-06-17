package com.example.todoapp.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
* Декоратор элементов списка TodoItem.
* @param leftOffset Отступ слева.
* @param topOffset Отступ сверху.
* @param rightOffset Отступ справа.
* @param bottomOffset Отступ снизу.
 */
class TodoItemDecorator (
    private val leftOffset: Int = 0,
    private val topOffset: Int = 0,
    private val rightOffset: Int = 0,
    private val bottomOffset: Int = 0)
    : RecyclerView.ItemDecoration() {

    /**
     * Устанавливает отступы для элемента списка.
     * @param outRect Прямоугольник, в котором определяются отступы.
     * @param view Вид элемента списка.
     * @param parent Родительский RecyclerView.
     * @param state Состояние RecyclerView.
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(leftOffset, topOffset, rightOffset, bottomOffset)
    }
}