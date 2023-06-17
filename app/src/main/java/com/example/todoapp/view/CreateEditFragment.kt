package com.example.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentCreateEditBinding
import com.example.todoapp.model.Importance
import com.example.todoapp.model.TodoItem
import com.example.todoapp.utils.navigator
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

/**
 * Фрагмент для создания и редактирования элемента списка задач.
 */
class CreateEditFragment : Fragment() {

    //    binding упрощает доступ к представлениям и повышает безопасность кода,
    //    предотвращая возможные ошибки NullPointerException при использовании _binding.

    // хранение привязки (binding) фрагмента
    private var _binding: FragmentCreateEditBinding? = null
    // свойство-делегат, которое обеспечивает доступ к привязке фрагмента
    private val binding get() = _binding!!


    // Целочисленное значение, переданное через аргументы фрагмента (см. onCreate()) необходимое для проверки новый/старый объект дела.
    private var myInt: Int = -1
    private val myDateFormat = SimpleDateFormat("dd MMM YYYY", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private var deadlineDate: Date? = null
    private lateinit var creationDate: Date
    companion object {
        private const val ARG_MY_INT = "arg_my_int"

        /**
         * Создает новый экземпляр [CreateEditFragment] с указанным целочисленным параметром.
         *
         * @param myInt Целочисленный параметр.
         * @return Новый экземпляр [CreateEditFragment].
         */
        fun newInstance(myInt: Int): CreateEditFragment {
            val fragment = CreateEditFragment()
            val args = Bundle()
            args.putInt(ARG_MY_INT, myInt)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myInt = it.getInt(ARG_MY_INT, -1)
        }
    }

    /**
     * Создает и возвращает пользовательский интерфейс фрагмента для отображения.
     *
     * @param inflater           Объект LayoutInflater для создания представления фрагмента.
     * @param container          Контейнер, в котором будет размещено представление фрагмента.
     * @param savedInstanceState Сохраненное состояние фрагмента, если есть.
     * @return Возвращаемое представление фрагмента.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreateEditBinding.inflate(inflater, container, false)
        val view = binding.root
        creationDate = Date()
        if (myInt >= 0){
            val todoItem = App.todoItemsRepository.getTodoItem(myInt)
            binding.editTextDescription.editText?.setText(todoItem.text)
            binding.spinnerPriority.setSelection(todoItem.importance.ordinal)
            if (todoItem.deadline != null){
                binding.switchCalendar.isChecked = true
                binding.dateText.text = myDateFormat.format(todoItem.deadline)
            }
            creationDate = todoItem.creationDate
        }
        binding.close.setOnClickListener {
            onCancelPressed()
        }

        // Настройка обработчиков нажатия на кнопки
        binding.save.setOnClickListener {
            // Извлечение данных из полей ввода
            val description = binding.editTextDescription.editText?.text.toString()
            val priority = binding.spinnerPriority.selectedItem.toString()
            val imp = when (priority){
                 "!! Высокий" -> Importance.HIGH
                 "Низкий"-> Importance.NORMAL
                 else -> Importance.LOW
            }
            // Проверка, новый объект или нет и запись даты изменения если старый
            val modify = if (myInt < 0) null else Date()
            // Сохранение элемента с использованием метода навигатора
            navigator().saveTodoItems(TodoItem("$myInt", description, imp, deadlineDate, false, creationDate, modify))
            onCancelPressed()
        }

        // Удаление элемента с использованием метода навигатора (сразу возварщение)
        binding.buttonDelete.setOnClickListener {
            if (myInt >= 0){
                App.todoItemsRepository.deleteTodoItemByInd(myInt)
                onCancelPressed()
            }
        }

        // Настройка DatePicker для выбора даты дедлайна
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Дата дедлайна").build()
        datePicker.addOnPositiveButtonClickListener {
            binding.dateText.text = myDateFormat.format(it)
            deadlineDate = Date(it)
        }
        // Обработчик переключения календаря
        binding.switchCalendar.setOnClickListener {
            if (binding.switchCalendar.isChecked){
                datePicker.show(this.parentFragmentManager, null)
            }
            else{
                deadlineDate = null
                binding.dateText.text = ""
            }
        }
        return view
    }

    /**
     * Обрабатывает нажатие на кнопку отмены.
     */
    private fun onCancelPressed() {
        navigator().showList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}