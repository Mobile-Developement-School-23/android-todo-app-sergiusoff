package com.example.todoapp.view

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.graphics.drawable.Drawable
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.ItemLongMenuBinding
import com.example.todoapp.databinding.PopupBinding
import com.example.todoapp.databinding.TodoItemBinding
import com.example.todoapp.model.Importance
import com.example.todoapp.model.TodoItem
import java.text.SimpleDateFormat
import java.util.*

/**
 * Адаптер для списка элементов дел.
 * @param todoItems Список элементов дел.
 * @param context Контекст приложения.
 */
class TodoItemAdapter(private var todoItems: List<TodoItem>, private val context: Context) :
    RecyclerView.Adapter<TodoItemAdapter.TodoItemViewHolder>(), ItemTouchSwapable {
    /**
     * ViewHolder для элементов списка дел.
     * @param view Представление элемента списка.
     */
    inner class TodoItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = TodoItemBinding.bind(view)
        private lateinit var todoItem: TodoItem
        // Здесь инициализируются (1 раз) различные графические ресурсы, используемые в представлении элемента списка
        private val checkedImage: Drawable = ContextCompat.getDrawable(context, R.drawable.checked)!!
        private val uncheckedImage: Drawable = ContextCompat.getDrawable(context, R.drawable.unchecked_normal)!!
        private val uncheckedHighImage: Drawable = ContextCompat.getDrawable(context, R.drawable.unchecked_high)!!
        private val doubleExclamation: Drawable = ContextCompat.getDrawable(context, R.drawable.double_exclamation)!!
        private val downArrow: Drawable = ContextCompat.getDrawable(context, R.drawable.down_arrow)!!
        private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        private val popupWindowInfo: PopupWindow = PopupWindow(context)
        private val popupWindowMenu: PopupWindow = PopupWindow(context)

        /**
         * Отображает информацию об элементе списка.
         * @param ind Индекс элемента.
         * @param view Представление элемента списка.
         */
        fun showInfo(ind: Int, view: View) {
            // Получаем данные о задаче по индексу
            val item = todoItems[ind]
            // Создаем представление для отображения информации о задаче
            val binding = PopupBinding.inflate(LayoutInflater.from(context))
            val contentView = binding.root
            // Конфигурируем PopupWindow для отображения информации
            popupWindowInfo.contentView = contentView
            popupWindowInfo.width = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindowInfo.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindowInfo.isOutsideTouchable = true
            popupWindowInfo.isFocusable = true
            // Заполняем представление информацией о задаче
            binding.textHeader.text = context.getString(R.string.item_text_text)
            binding.text.text = item.text
            binding.importance.text = context.getString(R.string.item_imp).format(item.importance.name)
            binding.create.text = context.getString(R.string.date_create).format(dateFormat.format(item.creationDate))

            val modifyText = if (item.lastModificationDate != null) {
                context.getString(R.string.date_modify).format(dateFormat.format(item.lastModificationDate!!))
            } else {
                context.getString(R.string.date_modify).format(context.getString(R.string.date_modify_no))
            }
            binding.modify.text = modifyText

            val deadlineText = if (item.deadline != null) {
                context.getString(R.string.deadline).format(dateFormat.format(item.deadline!!))
            } else {
                context.getString(R.string.deadline).format(context.getString(R.string.deadline_no))
            }
            binding.deadline.text = deadlineText
            // Создание прокрутки
            binding.text.movementMethod = ScrollingMovementMethod()
            // Отображаем PopupWindow рядом с представлением элемента списка
            popupWindowInfo.showAsDropDown(view)
        }

        /**
         * Отображает контекстное меню элемента списка.
         * @param ind Индекс элемента.
         * @param view Представление элемента списка.
         */
        fun showMenu(ind: Int, view: View){
            // Создаем представление для контекстного меню
            val binding = ItemLongMenuBinding.inflate(LayoutInflater.from(context))
            val contentView = binding.root
            // Конфигурируем PopupWindow для отображения контекстного меню
            popupWindowMenu.contentView = contentView
            popupWindowMenu.width = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindowMenu.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindowMenu.isOutsideTouchable = true
            popupWindowMenu.isFocusable = true
            // Обработка действий контекстного меню
            binding.buttonActivate.setOnClickListener {
                popupWindowMenu.dismiss()
                checkItem(ind)
            }

            binding.buttonEdit.setOnClickListener {
                popupWindowMenu.dismiss()
                adapterListener?.onEditClicked(adapterPosition)
            }

            binding.buttonDetails.setOnClickListener {
                popupWindowMenu.dismiss()
                showInfo(ind, view)
            }

            binding.buttonDelete.setOnClickListener {
                popupWindowMenu.dismiss()
                deleteItem(ind)
            }
            // Отображаем PopupWindow рядом с представлением элемента списка
            popupWindowMenu.showAsDropDown(view)
        }

        /**
         * Привязывает данные элемента списка к представлению.
         * @param newTodoItem Элемент списка дел.
         */
        fun bind(newTodoItem: TodoItem){

            todoItem = newTodoItem
            binding.todoItemText.text = todoItem.text
            // Все остальные операции связывания данных с представлением элемента списка
            if (todoItem.deadline != null){
                binding.todoItemDate.text = dateFormat.format(todoItem.deadline)
                binding.todoItemDate.visibility = View.VISIBLE
            } else {
                binding.todoItemDate.visibility = View.GONE
            }
            if (todoItem.isDone) {
                binding.todoItemText.setTextAppearance(R.style.body_is_done)
                binding.taskReadyFlag.setImageDrawable(checkedImage)
                binding.todoItemText.paintFlags = binding.todoItemText.paintFlags or STRIKE_THRU_TEXT_FLAG
                binding.taskImportance.setColorFilter(ContextCompat.getColor(context, R.color.green))
            }
            else{
                binding.todoItemText.setTextAppearance(R.style.body)
                binding.taskReadyFlag.setImageDrawable(uncheckedImage)
                binding.todoItemText.paintFlags = binding.todoItemText.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
                binding.taskImportance.clearColorFilter()
                if (!todoItem.isDone && todoItem.importance == Importance.HIGH){
                    binding.taskReadyFlag.setImageDrawable(uncheckedHighImage)
                    binding.taskImportance.setColorFilter(ContextCompat.getColor(context, R.color.red))
                }
            }
            when (todoItem.importance){
                Importance.HIGH -> {
                    binding.taskImportance.visibility = View.VISIBLE
                    binding.taskImportance.setImageDrawable(doubleExclamation)
                }
                Importance.LOW -> {
                    binding.taskImportance.visibility = View.VISIBLE
                    binding.taskImportance.setImageDrawable(downArrow)
                }
                else -> {binding.taskImportance.visibility = View.GONE}
            }
            binding.taskReadyFlag.setOnClickListener{ checkItem(adapterPosition) }
            binding.info.setOnClickListener { showInfo(adapterPosition, binding.info) }
            binding.root.setOnClickListener { adapterListener?.onEditClicked(adapterPosition) }
            binding.root.setOnLongClickListener { showMenu(adapterPosition, binding.info)
                true }
        }
    }

    private var adapterListener: AdapterListener? = null

    /**
     * Устанавливает слушатель для элементов адаптера.
     * @param listener Слушатель элементов адаптера.
     */
    fun setTodoItemListener(listener: AdapterListener) {
        adapterListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        // Создаем и возвращаем ViewHolder, содержащий представление элемента списка
        return TodoItemViewHolder(
            view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        // Вызываем метод bind() ViewHolder'а для связывания данных с представлением элемента списка
        holder.bind(todoItems[position])
    }

    override fun getItemCount(): Int = todoItems.size

    /**
     * Обновляет список элементов дел. Так как используется ТОЛЬКО при создании фрагмента,
     * подгружая данные с ToDoItemsrepository используется "ужасный" notifyDataSetChanged()
     * @param newTodoItems Новый список элементов дел.
     */
    fun updateItems(newTodoItems: List<TodoItem>) {
        todoItems = newTodoItems
        notifyDataSetChanged()
    }

    /**
     * Вызывается, когда элемент удаляется.
     * @param position Позиция удаляемого элемента.
     */
    override fun onItemDismiss(position: Int) = deleteItem(position)
    /**
     * Вызывается, когда элемент отмечен.
     * @param position Позиция отмеченного элемента.
     */
    override fun onItemChecked(position: Int) = checkItem(position)

    /**
    * Удаляет элемент из списка по указанной позиции.
    * @param position Позиция элемента, который должен быть удален.
     */
    private fun deleteItem(position: Int) {
        adapterListener?.onTodoItemDeleted(todoItems[position])
        notifyItemRemoved(position)
    }

    /**
     * Помечает элемент как отмеченный по указанной позиции.
     * @param position Позиция элемента, который должен быть отмечен.
     */
    private fun checkItem(position: Int) {
        adapterListener?.onTodoItemChecked(position)
        notifyItemChanged(position)
    }
}