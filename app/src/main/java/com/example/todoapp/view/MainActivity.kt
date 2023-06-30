package com.example.todoapp.view

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.R
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.databinding.FragmentTodoListBinding
import com.example.todoapp.model.Importance
import com.example.todoapp.model.TodoItem
import com.example.todoapp.retrofit.ApiEntity
import com.example.todoapp.retrofit.ApiResponseSerializer
import com.example.todoapp.retrofit.NetworkSchedulerService
import com.example.todoapp.retrofit.TodoItemSerializer
import com.example.todoapp.utils.Navigator
import com.example.todoapp.viewmodel.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Date
import java.util.UUID

/**
 * Основная активность приложения, реализующая навигацию между фрагментами и операции с элементами списка дел.
 */
class MainActivity : AppCompatActivity(), Navigator {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        val gson = GsonBuilder()
            .registerTypeAdapter(TodoItem::class.java, TodoItemSerializer())
            .registerTypeAdapter(ApiEntity::class.java, ApiResponseSerializer())
            .setPrettyPrinting()
            .create()

        val json = gson.toJson(createApiResponse())
        Log.d("MainActivity", "Serialized JSON:\n$json")

        val deserializedApi = gson.fromJson(json, ApiEntity::class.java) // Десериализация JSON строки в объект TodoItem
        Log.d("MainActivity", "Deserialized JSON:\n$deserializedApi")

        // Настройка цвета системной панели
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.back_primary)
        if (savedInstanceState == null) {
            // Добавление фрагмента TodoListFragment при первом создании активности
            supportFragmentManager.beginTransaction()
                .add(binding.container.id, TodoListFragment())
                .commit()
        }
        // Обработка нажатия на плавающую кнопку "Добавить"
        binding.floatingActionButton.setOnClickListener {
            showDetails(null)
        }

        // Скрытие плавающей кнопки "Добавить", если текущий фрагмент - CreateEditFragment (при пересоздании экрана)
        if (supportFragmentManager.findFragmentById(R.id.container) != null &&
            supportFragmentManager.findFragmentById(R.id.container) is CreateEditFragment){
            findViewById<FloatingActionButton>(R.id.floatingActionButton).hide()
        }

        sharedViewModel.todoItemProcess.observe(this) { event ->
            Log.d("MainActivityty", event.peekContent())
            event.getContentIfNotHandled()?.let { result ->
                Snackbar.make(binding.root, result, Snackbar.LENGTH_LONG).show()
            }
        }

        scheduleJob()
    }

    /**
     * Отображает фрагмент для создания/редактирования элемента списка дел.
     * @param id Индекс элемента, который нужно редактировать (-1 для создания нового элемента).
     */
    override fun showDetails(todoItem: TodoItem?) {
        binding.floatingActionButton.hide()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CreateEditFragment.newInstance(todoItem))
            .addToBackStack(null)
            .commit()
    }

    /**
     * Отображает список элементов и показывает плавающую кнопку "Добавить".
     */
    override fun showList() {
        binding.floatingActionButton.show()
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun scheduleJob() {
        val myJob = JobInfo.Builder(0, ComponentName(this, NetworkSchedulerService::class.java))
            .setRequiresCharging(true)
            .setMinimumLatency(1000)
            .setOverrideDeadline(2000)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true)
            .build()

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(myJob)
    }

    override fun onStop() {
        stopService(Intent(this, NetworkSchedulerService::class.java))
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        val startServiceIntent = Intent(this, NetworkSchedulerService::class.java)
        startService(startServiceIntent)
    }

    private fun createTodoItem(): TodoItem {
        // Создаем и возвращаем объект TodoItem
        val id = UUID.randomUUID()
        val text = "Sample todo item"
        val importance = Importance.BASIC
        val deadline = Date()
        val isDone = false
        val creationDate = Date()
        val lastModificationDate = Date()
        return TodoItem(id, text, importance, deadline, isDone, creationDate, lastModificationDate)
    }

    private fun createApiResponse(): ApiEntity {
        val status = "ok"
        val element = createTodoItem()
        val revision = 1
        return ApiEntity(status, null, listOf(element), revision)
    }
}