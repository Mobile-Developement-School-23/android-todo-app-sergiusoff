package com.example.todoapp.retrofit

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import com.example.todoapp.locateLazy
import com.example.todoapp.model.TodoItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("SpecifyJobSchedulerIdRange")
/**
 * Служба планировщика сетевых операций.
 */
class NetworkSchedulerService : JobService(), ConnectivityReceiver.ConnectivityReceiverListener {

    @SuppressLint("SpecifyJobSchedulerIdRange")
    private val TAG = NetworkSchedulerService::class.java.simpleName
    private lateinit var mConnectivityReceiver: ConnectivityReceiver
    private val repository: TodoItemsRepository by locateLazy()

    /**
     * Метод вызывается при создании службы.
     */
    @SuppressLint("SpecifyJobSchedulerIdRange")
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Service created")
        mConnectivityReceiver = ConnectivityReceiver(this)
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
        if (isConnected) {
            Log.i(TAG, "isConnected")
            // Запускаем корутину в IO-потоке
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Выполняем операции с использованием репозитория

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
                } catch (e: Exception) {
                    // Обработка ошибок
                }
            }
        }
    }
}