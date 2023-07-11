package com.example.todoapp.ui.view

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.todoapp.R
import com.example.todoapp.appComponent
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.model.TodoItem
import com.example.todoapp.utils.worker.UpdateDataWorker
import com.example.todoapp.utils.changeNetworkState.NetworkSchedulerService
import com.example.todoapp.ioc.DataSynchronizer
import com.example.todoapp.utils.Navigator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

/**
 * Основная активность приложения, реализующая навигацию между фрагментами и операции с элементами списка дел.
 */
class MainActivity : AppCompatActivity(), Navigator {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        // Создание экземпляра привязки для разметки activity_main.xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройка цвета системной панели
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.back_primary)

        // Добавление фрагмента TodoListFragment при первом создании активности
        if (savedInstanceState == null) {
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
            supportFragmentManager.findFragmentById(R.id.container) is CreateEditFragment
        ){
            findViewById<FloatingActionButton>(R.id.floatingActionButton).hide()
        }

        // Запуск планировщика задач
        scheduleJob()
    }

    @Inject
    fun checkAddEditResults(dataSynchronizer: DataSynchronizer){
        dataSynchronizer.todoItemProcess.observe(this){event ->
            // Получение и обработка события, если оно не было обработано ранее
            event.getContentIfNotHandled()?.let { result ->
                Snackbar.make(binding.root, result, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Отображает фрагмент для создания/редактирования элемента списка дел.
     * @param todoItem элемент, который нужно редактировать.
     */
    override fun showDetails(todoItem: TodoItem?) {
        binding.floatingActionButton.hide()

        // Замена текущего фрагмента на CreateEditFragment для создания/редактирования элемента
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

        // Удаление фрагментов из back stack, чтобы показать список элементов
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    /**
     * Планирует выполнение задачи через JobScheduler.
     */
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
        // Остановка службы NetworkSchedulerService при остановке активности
        stopService(Intent(this, NetworkSchedulerService::class.java))
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        // Запуск службы NetworkSchedulerService и запланированной работы UpdateDataWorker
        val startServiceIntent = Intent(this, NetworkSchedulerService::class.java)
        startService(startServiceIntent)
        UpdateDataWorker.enqueuePeriodicWork(this)
    }
}