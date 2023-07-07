package com.example.todoapp.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.databinding.FragmentCreateEditBinding
import com.example.todoapp.model.Importance
import com.example.todoapp.model.TodoItem
import com.example.todoapp.utils.navigator
import com.example.todoapp.viewmodel.CreateEditViewModel
import com.example.todoapp.viewmodel.SharedViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

/**
 * Фрагмент для создания и редактирования элемента списка задач.
 */
class CreateEditFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }
    //    binding упрощает доступ к представлениям и повышает безопасность кода,
    //    предотвращая возможные ошибки NullPointerException при использовании _binding.

    // хранение привязки (binding) фрагмента
    private var _binding: FragmentCreateEditBinding? = null
    // свойство-делегат, которое обеспечивает доступ к привязке фрагмента
    private val binding get() = _binding!!
    private lateinit var createEditViewModel: CreateEditViewModel


    // Целочисленное значение, переданное через аргументы фрагмента (см. onCreate()) необходимое для проверки новый/старый объект дела.
    private var tempItem: TodoItem? = null
    private val myDateFormat = SimpleDateFormat("dd MMM YYYY", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private var deadlineDate: Date? = null
    private lateinit var creationDate: Date
    companion object {
        private const val ARG_MY_ITEM = "arg_my_int"

        /**
         * Создает новый экземпляр [CreateEditFragment] с указанным TodoItem параметром.
         *
         * @param todoItem TodoItem параметр.
         * @return Новый экземпляр [CreateEditFragment].
         */
        fun newInstance(todoItem: TodoItem?): CreateEditFragment {
            val fragment = CreateEditFragment()
            val args = Bundle()
            args.putSerializable(ARG_MY_ITEM, todoItem)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tempItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(ARG_MY_ITEM, TodoItem::class.java)
            } else {
                it.getSerializable(ARG_MY_ITEM) as TodoItem?
            }
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
        ViewModelProvider(this)[CreateEditViewModel::class.java].sharedViewModel = sharedViewModel
        createEditViewModel = ViewModelProvider(this)[CreateEditViewModel::class.java]
        val item = tempItem
        if (item != null){
            Log.d("KEEEEEEEEK", "$item")
            binding.editTextDescription.editText?.setText(item.text)
            binding.spinnerPriority.setSelection(item.importance.ordinal)
            if (item.deadline != null){
                binding.switchCalendar.isChecked = true
                binding.dateText.text = myDateFormat.format(item.deadline)
            }
            creationDate = item.creationDate
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
                 "!! Высокий" -> Importance.IMPORTANT
                 "Низкий"-> Importance.BASIC
                 else -> Importance.LOW
            }
            // Проверка, новый объект или нет и запись даты изменения если старый
            val modify = Date()
            if (item == null)
                createEditViewModel.saveTodoItem(TodoItem(UUID.randomUUID(), description, imp, deadlineDate, false, creationDate, modify))
            else
                createEditViewModel.updateTodoItem(TodoItem(item.id, description, imp, deadlineDate, false, creationDate, modify))
            onCancelPressed()
        }

        // Удаление элемента с использованием метода навигатора (сразу возварщение)
        binding.buttonDelete.setOnClickListener {
            if (item != null){
                createEditViewModel.deleteTodoItem(item)
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