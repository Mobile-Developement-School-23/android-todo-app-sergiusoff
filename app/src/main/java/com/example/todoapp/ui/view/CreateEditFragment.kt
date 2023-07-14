package com.example.todoapp.ui.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todoapp.R
import com.example.todoapp.appComponent
import com.example.todoapp.databinding.FragmentCreateEditBinding
import com.example.todoapp.ioc.CreateEditViewModelFactory
import com.example.todoapp.model.Importance
import com.example.todoapp.model.TodoItem
import com.example.todoapp.ui.stateholders.CreateEditViewModel
import com.example.todoapp.utils.navigator
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
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

    // Целочисленное значение, переданное через аргументы фрагмента (см. onCreate())
    // необходимое для проверки новый/старый объект дела.
    private var tempItem: TodoItem? = null
     val myDateFormat = SimpleDateFormat("dd MMM YYYY", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private var deadlineDate: Date? = null
    private lateinit var creationDate: Date
    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var timePicker: MaterialTimePicker


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
        else{
            binding.priorityAsText.text = requireContext().resources.getStringArray(R.array.priority_array)[0]
        }
        binding.close.setOnClickListener {
            onCancelPressed()
        }
        setupButtonClickListeners()
        setupDatePicker()
        setupTimePicker()
        return view
    }

    /**
     * Настраивает поля формы на основе объекта [item] типа [TodoItem].
     *
     * @param item Объект [TodoItem], содержащий данные для заполнения полей формы.
     */
    private fun setupFields(item: TodoItem) {
        binding.editTextDescription.editText?.setText(item.text)
//        binding.spinnerPriority.setSelection(item.importance.ordinal)
        binding.priorityAsText.text = requireContext().resources
            .getStringArray(R.array.priority_array)[item.importance.ordinal]
        deadlineDate = item.deadline
        if (deadlineDate != null) {
            updateDateTimeFields()
            binding.switchCalendar.isChecked = true
        }
        creationDate = item.creationDate
    }

    private fun updateDateTimeFields(){
        binding.dateText.visibility = VISIBLE
        binding.timeText.visibility = VISIBLE
        if (deadlineDate == null){
            deadlineDate = Date()
        }
        binding.dateText.text = myDateFormat.format(deadlineDate)
        val calendar = Calendar.getInstance()
        calendar.time = deadlineDate
        binding.timeText.text =  String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE))
    }

    /**
     * Настраивает обработчики нажатия на кнопки формы.
     */
    private fun setupButtonClickListeners() {
        binding.save.setOnClickListener {
            val description = binding.editTextDescription.editText?.text.toString()
            val imp = when (binding.priorityAsText.text) {
                "!! Высокий" -> Importance.IMPORTANT
                "Низкий" -> Importance.BASIC
                else -> Importance.LOW
            }
            val modify = Date()
            if (tempItem == null)
            // Если [tempItem] равен null, создается новый [TodoItem] с использованием UUID.randomUUID()
                createEditViewModel.saveTodoItem(TodoItem(UUID.randomUUID(), description, imp,
                    deadlineDate, false, creationDate, modify))
            else {
                // В противном случае, обновляется существующий [TodoItem]
                tempItem?.text = description
                tempItem?.deadline = deadlineDate
                tempItem?.importance = imp
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

        binding.switchCalendar.setOnClickListener {
            if (binding.switchCalendar.isChecked) {
                deadlineDate = tempItem?.deadline
                updateDateTimeFields()
            } else {
                binding.dateText.visibility = GONE
                binding.timeText.visibility = GONE
                deadlineDate = null
            }
        }

        binding.dateText.setOnClickListener {
            datePicker.show(this.parentFragmentManager, null)
        }

        binding.timeText.setOnClickListener {
            timePicker.show(this.parentFragmentManager, null)
        }

        binding.priorityAsText.setOnClickListener {
            showPriorityBottomSheet()
        }
    }

    private fun showPriorityBottomSheet() {
        val bottomSheetDialogFragment = PriorityBottomSheetDialogFragment()
        bottomSheetDialogFragment.setOnPrioritySelectedListener { priority ->
            binding.priorityAsText.text = priority
            if (priority == "!! Высокий") {
                animatePrioritySelection()
            }
        }
        bottomSheetDialogFragment.show(parentFragmentManager, "PriorityBottomSheetDialog")
    }

    private fun animatePrioritySelection() {
        val originalTextColor = binding.priorityAsText.currentTextColor // Сохраняем исходный цвет текста
        val highlightColor = Color.parseColor("#FF0000") // Цвет подсветки (красный)

        // Создаем анимацию изменения фона текстового поля
        val highlightAnimation = ObjectAnimator.ofArgb(binding.priorityAsText, "textColor", highlightColor)
        highlightAnimation.duration = 200 // Длительность анимации (в миллисекундах)

        // Создаем анимацию возвращения исходного фона текстового поля
        val revertAnimation = ObjectAnimator.ofArgb(binding.priorityAsText, "textColor", originalTextColor)
        revertAnimation.startDelay = 200 // Задержка перед выполнением анимации возврата
        revertAnimation.duration = 200 // Длительность анимации возврата

        // Создаем AnimatorSet для объединения анимаций подсветки и возврата
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(highlightAnimation, revertAnimation)

        animatorSet.start() // Запускаем анимацию
    }

    /**
     * Настраивает DatePicker для выбора даты дедлайна.
     */
    private fun setupDatePicker() {
        datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Дата дедлайна").build()
        datePicker.addOnPositiveButtonClickListener {
            binding.dateText.text = myDateFormat.format(it)
            deadlineDate = Date(it)
        }
    }

    private fun setupTimePicker() {
        var hours: Int = 0
        var minutes: Int = 0
        if (deadlineDate != null){
            val calendar = Calendar.getInstance()
            calendar.time = deadlineDate
            hours = calendar.get(Calendar.HOUR_OF_DAY)
            minutes = calendar.get(Calendar.MINUTE)
        }
        timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(hours)
            .setMinute(minutes)
            .setTitleText("Время дедлайна")
            .build()
        timePicker.addOnPositiveButtonClickListener {
            binding.timeText.text = String.format("%02d:%02d", timePicker.hour, timePicker.minute)
            addHourAndMinutes(timePicker.hour, timePicker.minute)
        }
    }

    private fun addHourAndMinutes(hours: Int, minutes: Int){
        val calendar = Calendar.getInstance()
        calendar.time = deadlineDate
        calendar.set(Calendar.HOUR_OF_DAY, hours)
        calendar.set(Calendar.MINUTE, minutes)
        deadlineDate = calendar.time
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