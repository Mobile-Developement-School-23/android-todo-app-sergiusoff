package com.example.todoapp.utils.changeNetworkState

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log
import com.example.todoapp.App
import com.example.todoapp.model.TodoItemsRepository
import com.example.todoapp.retrofit.model.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("SpecifyJobSchedulerIdRange")
/**
 * Служба планировщика сетевых операций.
 */
class NetworkSchedulerService : JobService(), ConnectivityReceiver.ConnectivityReceiverListener {

    @SuppressLint("SpecifyJobSchedulerIdRange")
    private val TAG = NetworkSchedulerService::class.java.simpleName
    private lateinit var mConnectivityReceiver: ConnectivityReceiver

    @Inject
    lateinit var repository: TodoItemsRepository

    private var sharedPreferences: SharedPreferences? = null

    /**
     * Метод вызывается при создании службы.
     */
    @SuppressLint("SpecifyJobSchedulerIdRange")
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Service created")
        (applicationContext as App).appComponent.inject(this)
        mConnectivityReceiver = ConnectivityReceiver(this)
        sharedPreferences = applicationContext.getSharedPreferences("todoItemApp", Context.MODE_PRIVATE)
    }

    /**
     * Метод вызывается при запуске команды службы.
     */
    @SuppressLint("SpecifyJobSchedulerIdRange")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        return START_NOT_STICKY
    }

    /**
     * Метод вызывается при запуске задачи службы.
     */
    @SuppressLint("SpecifyJobSchedulerIdRange")
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(TAG, "onStartJob")
        // Регистрируем ConnectivityReceiver для отслеживания изменений подключения
        registerReceiver(mConnectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        return true
    }

    /**
     * Метод вызывается при остановке задачи службы.
     */
    override fun onStopJob(params: JobParameters?): Boolean {
        Log.i(TAG, "onStopJob")
        // Отменяем регистрацию ConnectivityReceiver
        unregisterReceiver(mConnectivityReceiver)
        return true
    }

    /**
     * Метод вызывается при изменении состояния сетевого подключения.
     * При включении (после выключения или при запуске) отправляем с телефона на сервер
     * @param isConnected Флаг, указывающий наличие подключения к сети.
     */
    @SuppressLint("SpecifyJobSchedulerIdRange")
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        Log.i(TAG, "onNetworkConnectionChanged")
        if (isConnected && sharedPreferences!!.getBoolean("localChanged", false)) {
            Log.i(TAG, "isConnected")
            // Запускаем корутину в IO-потоке
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Выполняем операции с использованием репозитория
                    Log.d("NetworkSchedulerService", "I AM IN NETWORK 0")
                    Log.d("NetworkSchedulerService", "I AM IN NETWORK 1")
                    // Отправка всех элементов на удаленный сервер
                    when (val response = repository.postAllItemsOnBack(repository.getAll().first())) {
                        is NetworkResult.Success -> {
                            // Выводим статус ответа
                            Log.i(TAG, response.data.status ?: "ok")
                        }
                        is NetworkResult.Error -> {
                            // Выводим сообщение об ошибке
                            Log.i(TAG, response.errorMessage)
                        }
                    }
                    Log.d("NetworkSchedulerService", "I AM IN NETWORK 2")

                } catch (e: Exception) {
                    Log.d("NetworkSchedulerService", e.toString())
                }
            }
        }
    }
}