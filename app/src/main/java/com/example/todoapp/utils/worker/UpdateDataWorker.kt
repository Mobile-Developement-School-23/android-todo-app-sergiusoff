package com.example.todoapp.utils.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.todoapp.model.TodoItemsRepository
import com.example.todoapp.retrofit.model.NetworkResult
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Рабочий класс для выполнения периодического обновления данных в фоновом режиме.
 *
 * @param context Контекст приложения.
 * @param params Параметры рабочего.
 */
class UpdateDataWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    private val repository: TodoItemsRepository
) : CoroutineWorker(context, params) {

    /**
    *    Функция, которая выполняет работу рабочего исполнителя.
    *    Здесь происходит вызов метода getAllItemsFromBack репозитория для получения всех элементов задач с сервера.
    *    @return Результат работы рабочего исполнителя в виде объекта [Result].
    */
    override suspend fun doWork(): Result {
        Log.d("PERIODIC WORK", "Before try")
        try {
            when (val result = repository.getAllItemsFromBack()) {
                is NetworkResult.Success -> {
                    // В случае успешного получения данных, очищаем и обновляем список задач и отображаем Snackbar
                    repository.clearAndInsertAllItems(result.data.list!!)
                }
                is NetworkResult.Error -> {
                    // Уведомление о том что не получилось обновить в фоне, мб предложение
                // отключить попытки пробраться в сеть на определённый период
//                    val errorMessage = result.errorMessage
//                    val exception = result.exception

                }
            }
            Log.d("PERIODIC WORK", "Mid try")
            return Result.success()
        } catch (e: Exception) {
            Log.d("PERIODIC WORK", e.message.toString())
            return Result.retry()
        }
    }

    @Module
    abstract class Builder {
        @Binds
        @IntoMap
        @WorkerKey(UpdateDataWorker::class)
        abstract fun bindHelloWorldWorker(worker: UpdateDataWorker): CoroutineWorker
    }

    companion object {

        /**
         * Функция для запуска периодической работы рабочего исполнителя.
         * Создается периодический запрос работы с заданными интервалом и ограничениями.
         * Запускается рабочий исполнитель с уникальным идентификатором.
         */
        fun enqueuePeriodicWork(context: Context) {

            // Определение ограничений для работы
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresBatteryNotLow(false)
                .build()

            // Создание периодического запроса работы
            val periodicWorkRequest = PeriodicWorkRequestBuilder<UpdateDataWorker>(
                repeatInterval = 8,
                repeatIntervalTimeUnit = TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .build()

            // Запуск периодической работы рабочего исполнителя
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "UpdateDataWorker",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
        }
    }
}