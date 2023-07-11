package com.example.todoapp.ui.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.todoapp.appComponent
import com.example.todoapp.databinding.FragmentCreateEditBinding
import com.example.todoapp.model.Importance
import com.example.todoapp.model.TodoItem
import com.example.todoapp.utils.navigator
import com.example.todoapp.ioc.CreateEditViewModelFactory
import com.example.todoapp.ui.stateholders.CreateEditViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Фрагмент для создания и редактирования элемента списка задач.
 */
class CreateEditFragment : Fragment() {
    // binding упрощает доступ к представлениям и повышает безопасность кода,
    // предотвращая возможные ошибки NullPointerException при использовании _binding.
    // хранение привязки (binding) фрагмента
    private var _binding: FragmentCreateEditBinding? = null
    // свойство-делегат, которое обеспечивает доступ к привязке фрагмента
    private val binding get() = _binding!!

    // Целочисленное значение, переданное через аргументы фрагмента (см. onCreate()) необходимое для проверки новый/старый объект дела.
    private var tempItem: TodoItem? = null
    private val myDateFormat = SimpleDateFormat("dd MMM YYYY", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private var deadlineDate: Date? = null
    private lateinit var creationDate: Date


    private val createEditViewModel: CreateEditViewModel by viewModels {
        factory.create(tempItem?:TodoItem(UUID.randomUUID(), "", Importance.LOW, Date(), false, Date(), Date()))
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    @Inject
    lateinit var factory: CreateEditViewModelFactory.Factory
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
        val item = tempItem
        if (item != null) {
            setupFields(item)
        }
        binding.close.setOnClickListener {
            onCancelPressed()
        }
        setupButtonClickListeners()
        setupDatePicker()
        return view
    }

    /**
     * Настраивает поля формы на основе объекта [item] типа [TodoItem].
     *
     * @param item Объект [TodoItem], содержащий данные для заполнения полей формы.
     */
    private fun setupFields(item: TodoItem) {
        binding.editTextDescription.editText?.setText(item.text)
        binding.spinnerPriority.setSelection(item.importance.ordinal)
        if (item.deadline != null) {
            binding.switchCalendar.isChecked = true
            binding.dateText.text = myDateFormat.format(item.deadline)
        }
        creationDate = item.creationDate
    }

    /**
     * Настраивает обработчики нажатия на кнопки формы.
     */
    private fun setupButtonClickListeners() {
        binding.save.setOnClickListener {
            val description = binding.editTextDescription.editText?.text.toString()
            val priority = binding.spinnerPriority.selectedItem.toString()
            val imp = when (priority) {
                "!! Высокий" -> Importance.IMPORTANT
                "Низкий" -> Importance.BASIC
                else -> Importance.LOW
            }
            val modify = Date()
            if (tempItem == null)
            // Если [tempItem] равен null, создается новый [TodoItem] с использованием UUID.randomUUID()
                createEditViewModel.saveTodoItem(TodoItem(UUID.randomUUID(), description, imp, deadlineDate, false, creationDate, modify))
            else {
                // В противном случае, обновляется существующий [TodoItem]
                tempItem?.text = description
                createEditViewModel.updateTodoItem()
            }
            onCancelPressed()
        }

        binding.buttonDelete.setOnClickListener {
            if (tempItem != null) {
                createEditViewModel.deleteTodoItem(tempItem!!)
                onCancelPressed()
            }
        }
    }

    /**
     * Настраивает DatePicker для выбора даты дедлайна.
     * При выборе даты обновляет текстовое поле [dateText] и переменную [deadlineDate].
     */
    private fun setupDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Дата дедлайна").build()
        datePicker.addOnPositiveButtonClickListener {
            binding.dateText.text = myDateFormat.format(it)
            deadlineDate = Date(it)
        }
        binding.switchCalendar.setOnClickListener {
            if (binding.switchCalendar.isChecked) {
                datePicker.show(this.parentFragmentManager, null)
            } else {
                deadlineDate = null
                binding.dateText.text = ""
            }
        }
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