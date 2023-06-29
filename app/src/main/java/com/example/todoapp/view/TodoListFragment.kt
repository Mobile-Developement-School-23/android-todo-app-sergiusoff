package com.example.todoapp.view

import android.os.Bundle
import android.view.*
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTodoListBinding
import com.example.todoapp.model.TodoItem
import com.example.todoapp.utils.navigator
import com.example.todoapp.viewmodel.TodoListViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Фрагмент списка задач TodoList.
 */
class TodoListFragment : Fragment(), AdapterListener {
    private lateinit var todoListViewModel: TodoListViewModel
    private lateinit var binding: FragmentTodoListBinding
    private lateinit var todoItemAdapter: TodoItemAdapter
    private var job: Job? = null

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
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
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
        todoListViewModel = ViewModelProvider(this)[TodoListViewModel::class.java]
        // Запуск наблюдателя жизненного цикла фрагмента для отслеживания изменений в списке задач

        job = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
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

        // понятия не имею что тут нужно
        binding.showRdyIcon.setOnClickListener {
//            todoListViewModel.checkRetrofitWork()

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
        job?.cancel()
    }
}