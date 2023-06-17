package com.example.todoapp.utils

import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.todoapp.R
import kotlin.math.abs

/**
 * Вспомогательный класс для отрисовки фона и иконки при свайпе элемента.
 */
class SwipeBackgroundHelper {
    companion object {

        // когда рисовать серый фон
        private const val OFFSET_PX = 8

        /**
         * Внутренний класс, представляющий команду отрисовки иконки и фона.
         * @property icon Иконка для отрисовки.
         * @property backgroundColor Цвет фона.
         */
        private class DrawCommand constructor(val icon: Drawable, val backgroundColor: Int)

        /**
         * Создает команду отрисовки для указанного элемента и смещения.
         * @param viewItem Элемент списка.
         * @param dX Смещение по оси X.
         * @param iconResId Идентификатор ресурса иконки.
         * @return Команда отрисовки.
         */
        private fun createDrawCommand(viewItem: View, dX: Float, iconResId: Int): DrawCommand {
            // Определение иконки и ее цвета
            val context = viewItem.context
            var icon = ContextCompat.getDrawable(context, iconResId)
            icon = DrawableCompat.wrap(icon!!).mutate()
            icon.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(context, R.color.white),
                PorterDuff.Mode.SRC_IN
            )
            // Определение цвета фона
            val backgroundColor = getBackgroundColor(if (dX < 0) R.color.red else R.color.green, R.color.gray, dX, viewItem)
            return DrawCommand(icon, backgroundColor)
        }

        /**
         * Определяет цвет фона в зависимости от смещения и ширины элемента.
         *
         * @param firstColor Цвет фона при выполнении действия.
         * @param secondColor Цвет фона при отмене действия.
         * @param dX Смещение по оси X.
         * @param viewItem Элемент списка.
         * @return Цвет фона.
         */
        private fun getBackgroundColor(firstColor: Int, secondColor: Int, dX: Float, viewItem: View): Int {
            return when (willActionBeTriggered(dX, viewItem.width)) {
                true -> ContextCompat.getColor(viewItem.context, firstColor)
                false -> ContextCompat.getColor(viewItem.context, secondColor)
            }
        }

        /**
         * Определяет, будет ли выполнено действие на основе смещения и ширины элемента.
         *
         * @param dX Смещение по оси X.
         * @param viewWidth Ширина элемента.
         * @return `true`, если действие будет выполнено, `false` в противном случае.
         */
        private fun willActionBeTriggered(dX: Float, viewWidth: Int): Boolean {
            return abs(dX) >= viewWidth / OFFSET_PX
        }

        /**
         * Вычисляет верхний отступ для иконки.
         *
         * @param icon Иконка.
         * @param viewItem Элемент списка.
         * @return Верхний отступ.
         */
        private fun calculateTopMargin(icon: Drawable, viewItem: View): Int {
            return (viewItem.height - icon.intrinsicHeight) / 2
        }


        /**
         * Возвращает прямоугольник, определяющий расположение иконки при отрисовке с заданным смещением.
         * @param viewItem Элемент списка.
         * @param iconWidth Ширина иконки.
         * @param topMargin Верхний отступ иконки.
         * @param sideOffset Боковое смещение.
         * @param dX Смещение по оси X.
         * @return Прямоугольник расположения иконки.
         */
        private fun getStartContainerRectangle(
            viewItem: View,
            iconWidth: Int,
            topMargin: Int,
            sideOffset: Int,
            dX: Float
        ): Rect {
            if (dX > 0)
                return Rect(viewItem.left + dX.toInt() / 2 - iconWidth - sideOffset, viewItem.top + topMargin,
                    viewItem.left + dX.toInt() / 2 - sideOffset, viewItem.bottom - topMargin)
            return Rect(viewItem.right + dX.toInt() / 2 + sideOffset, viewItem.top + topMargin,
                viewItem.right + dX.toInt() / 2 + iconWidth + sideOffset, viewItem.bottom - topMargin)
        }

        /**
         * Отрисовывает иконку на холсте с заданным смещением и элементом списка.
         * @param canvas Холст.
         * @param viewItem Элемент списка.
         * @param dX Смещение по оси X.
         * @param icon Иконка.
         */
        private fun drawIcon(canvas: Canvas, viewItem: View, dX: Float, icon: Drawable) {
            val topMargin = calculateTopMargin(icon, viewItem)
            icon.bounds = getStartContainerRectangle(viewItem, icon.intrinsicWidth, topMargin, OFFSET_PX, dX)
            icon.draw(canvas)
        }

        /**
         * Возвращает прямоугольник фона для отрисовки с заданным смещением.
         * @param viewItem Элемент списка.
         * @param dX Смещение по оси X.
         * @return Прямоугольник фона.
         */
        private fun getBackGroundRectangle(viewItem: View, dX: Float): RectF {
            if (dX > 0)
                return RectF(0f, viewItem.top.toFloat(), dX, viewItem.bottom.toFloat())
            return RectF(viewItem.right.toFloat() + dX, viewItem.top.toFloat(),
                viewItem.right.toFloat(), viewItem.bottom.toFloat())
        }

        /**
         * Отрисовывает фон на холсте с заданным смещением и элементом списка.
         * @param canvas Холст.
         * @param viewItem Элемент списка.
         * @param dX Смещение по оси X.
         * @param color Цвет фона.
         */
        private fun drawBackground(canvas: Canvas, viewItem: View, dX: Float, color: Int) {
            val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            backgroundPaint.color = color
            val backgroundRectangle = getBackGroundRectangle(viewItem, dX)
            println("KEEEEEEK $backgroundRectangle $backgroundPaint")
            canvas.drawRect(backgroundRectangle, backgroundPaint)
        }

        /**
         * Производит отрисовку команды на холсте с заданным смещением и элементом списка.
         * @param drawCommand Команда отрисовки.
         * @param canvas Холст.
         * @param dX Смещение по оси X.
         * @param viewItem Элемент списка.
         */
        private fun paintDrawCommand(drawCommand: DrawCommand, canvas: Canvas, dX: Float, viewItem: View) {
            drawBackground(canvas, viewItem, dX, drawCommand.backgroundColor)
            drawIcon(canvas, viewItem, dX, drawCommand.icon)
        }

        /**
         * Отрисовывает команду на холсте при смещении влево.
         * @param canvas Холст.
         * @param viewItem Элемент списка.
         * @param iconResId Идентификатор ресурса иконки.
         * @param dX Смещение по оси X.
         */
        fun paintDrawCommandToStart(canvas: Canvas, viewItem: View, @DrawableRes iconResId: Int, dX: Float) {
            val drawCommand = createDrawCommand(viewItem, dX, iconResId)
            paintDrawCommand(drawCommand, canvas, dX, viewItem)
        }
    }
}