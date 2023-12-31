package com.example.todoapp.ui.view

import android.content.Context
import android.os.Bundle
import android.view.*

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.appComponent
import com.example.todoapp.databinding.FragmentTodoListBinding
import com.example.todoapp.model.TodoItem
import com.example.todoapp.utils.navigator
import com.example.todoapp.ioc.TodoListViewModelFactory
import com.example.todoapp.ui.stateholders.TodoListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Фрагмент списка задач TodoList.
 */
class TodoListFragment : Fragment(), AdapterListener {
    private val todoListViewModel: TodoListViewModel by viewModels { factory }
    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var todoItemAdapter: TodoItemAdapter

    @Inject
    lateinit var factory: TodoListViewModelFactory

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    /**
     * Создает и возвращает представление фрагмента.
     *
     * @param inflater Инфлейтер для создания представления.
     * @param container Родительская ViewGroup.
     * @param savedInstanceState Сохраненное состояние фрагмента.
     * @return Возвращаемое представление фрагмента.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Вызывается, когда представление фрагмента было создано.
     *
     * @param view Созданное представление фрагмента.
     * @param savedInstanceState Сохраненное состояние фрагмента.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addAdapter()
        addObserversAndListeners(view)
    }

    private fun addAdapter() {
        // Создание адаптера для списка задач
        todoItemAdapter = TodoItemAdapter(emptyList(), requireContext())
        todoItemAdapter.setTodoItemListener(this)
        // Настройка адаптера и макета для списка задач
        binding.todosView.adapter = todoItemAdapter
        binding.todosView.layoutManager = LinearLayoutManager(requireContext())
        binding.todosView.addItemDecoration(TodoItemDecorator(bottomOffset = 16))
        // Создание объекта ItemTouchHelper для обработки жестов
        val itemTouchHelperCallback = ItemTouchHelperCallback(todoItemAdapter)

        // Запуск наблюдателя жизненного цикла фрагмента для отслеживания изменений в списке задач
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                todoListViewModel.todoItems.collect {
                    todoItemAdapter.updateItems(it)
                    binding.subtitle.text = getString(R.string.rdy_count)
                        .format(it.count { item -> item.isDone }, it.size)
                }
            }
        }

        val touchHelper = ItemTouchHelper(itemTouchHelperCallback)
        touchHelper.attachToRecyclerView(binding.todosView)
    }

    private fun addObserversAndListeners(view: View) {
        // Получение экземпляра ViewModel для управления списком задач
        todoListViewModel.showSnackbarEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { errorMessage ->
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show()
            }
        }

        // Настройка слушателя изменения состояния панели приложения
        binding.appbarLayout.addOnOffsetChangedListener { appBar, offset ->
            val seekPosition = -offset / appBar.totalScrollRange.toFloat()
            binding.motionLayout.progress = seekPosition
        }

        // обновление данных с сервера
        binding.refreshBtn.setOnClickListener {
            todoListViewModel.fetchData()
        }
    }

    /**
     * Вызывается при удалении задачи.
     * @param item Удаленная задача.
     */
    override fun onTodoItemDeleted(item: TodoItem) {
        todoListViewModel.deleteTodoItem(item)
    }

    /**
     * Вызывается при отметке задачи.
     * @param position Позиция отмеченной задачи.
     */
    override fun onTodoItemChecked(item: TodoItem) {
        todoListViewModel.updateTodoItem(item)
    }

    /**
     * Вызывается при нажатии на кнопку редактирования.
     * @param position Позиция задачи для редактирования.
     */
    override fun onEditClicked(todoItem: TodoItem) {
        navigator().showDetails(todoItem)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}