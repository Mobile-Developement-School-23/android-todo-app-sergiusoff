package com.example.todoapp.view

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.utils.SwipeBackgroundHelper

/**
 * Callback-класс для обработки жестов перемещения и свайпа элементов в RecyclerView.
 *
 * @param adapter Адаптер, реализующий интерфейс [ItemTouchSwapable].
 */
class ItemTouchHelperCallback(val adapter: ItemTouchSwapable) : ItemTouchHelper.Callback() {

    /**
     * Определяет разрешенные типы движений элементов в RecyclerView.
     *
     * @param recyclerView RecyclerView, содержащий элементы.
     * @param viewHolder ViewHolder элемента, для которого определяются типы движений.
     * @return Флаги, определяющие разрешенные типы движений элементов.
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = 0 // Запрещены перемещения элементов
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END // Разрешены свайпы влево и вправо
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    /**
     * Вызывается при попытке перемещения элемента в RecyclerView.
     *
     * @param recyclerView RecyclerView, содержащий элементы.
     * @param viewHolder ViewHolder элемента, который пытаются переместить.
     * @param target ViewHolder целевого элемента, на который выполняется перемещение.
     * @return true, если перемещение разрешено; в противном случае - false.
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Вызывается при отрисовке перемещения или свайпа элемента в RecyclerView.
     *
     * @param c Canvas для рисования.
     * @param recyclerView RecyclerView, содержащий элементы.
     * @param viewHolder ViewHolder элемента, который перемещается или свайпается.
     * @param dX Смещение по оси X.
     * @param dY Смещение по оси Y.
     * @param actionState Текущее состояние действия (перемещение или свайп).
     * @param isCurrentlyActive true, если элемент активен (перемещается или свайпается); в противном случае - false.
     */
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            println(dX) // Выводим значение смещения по оси X в консоль (для отладки)
            val viewItem = viewHolder.itemView // Получаем View элемента
            val sym = if (dX < 0) R.drawable.delete else R.drawable.check // Определяем иконку в зависимости от направления свайпа
            SwipeBackgroundHelper.paintDrawCommandToStart(c, viewItem, sym, dX) // Рисуем задний фон элемента с иконкой
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    /**
     * Вызывается при свайпе элемента в RecyclerView.
     *
     * @param viewHolder ViewHolder свайпнутого элемента.
     * @param direction Направление свайпа.
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.START) {
            adapter.onItemDismiss(viewHolder.adapterPosition) // Вызываем метод адаптера для удаления элемента
        } else {
            adapter.onItemChecked(viewHolder.adapterPosition) // Вызываем метод адаптера для пометки элемента как выполненного
        }
    }
}