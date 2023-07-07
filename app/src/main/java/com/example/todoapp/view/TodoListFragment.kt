package com.example.todoapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.example.todoapp.viewmodel.TodoListViewModel
import com.example.todoapp.viewmodel.TodoListViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.fragment.app.viewModels

/**
 * Фрагмент списка задач TodoList.
 */
class TodoListFragment : Fragment(), AdapterListener {
//    private lateinit var todoListViewModel: TodoListViewModel
    private val todoListViewModel: TodoListViewModel by viewModels { factory }
    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var todoItemAdapter: TodoItemAdapter
    private var jobs: MutableList<Job> = mutableListOf()

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

        // Создание адаптера для списка задач
        todoItemAdapter = TodoItemAdapter(emptyList(), requireContext())
        todoItemAdapter.setTodoItemListener(this)
        // Настройка адаптера и макета для списка задач
        binding.todosView.adapter = todoItemAdapter
        binding.todosView.layoutManager = LinearLayoutManager(requireContext())
        binding.todosView.addItemDecoration(TodoItemDecorator(bottomOffset = 16))
        // Создание объекта ItemTouchHelper для обработки жестов
        val itemTouchHelperCallback = ItemTouchHelperCallback(todoItemAdapter)
        val touchHelper = ItemTouchHelper(itemTouchHelperCallback)
        touchHelper.attachToRecyclerView(binding.todosView)
        // Получение экземпляра ViewModel для управления списком задач
//        todoListViewModel = ViewModelProvider(this)[TodoListViewModel::class.java]


        todoListViewModel.showSnackbarEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { errorMessage ->
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show()
            }
        }

        // Запуск наблюдателя жизненного цикла фрагмента для отслеживания изменений в списке задач

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.d("NetworkSchedulerService", "UPDATE_VIEW")
                todoListViewModel.todoItems.collect {
                    todoItemAdapter.updateItems(it)
                    binding.subtitle.text = getString(R.string.rdy_count)
                        .format(it.count { item -> item.isDone }, it.size)
                }
            }
        }

        // Настройка слушателя изменения состояния панели приложения
        binding.appbarLayout.addOnOffsetChangedListener { appBar, offset ->
            val seekPosition = -offset / appBar.totalScrollRange.toFloat()
            binding.motionLayout.progress = seekPosition
        }

        // Пусть будет refresh пока вместо глазика
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