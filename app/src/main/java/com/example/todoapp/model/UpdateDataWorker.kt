package com.example.todoapp.model

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.todoapp.locateLazy
import java.util.concurrent.TimeUnit

/**
 * Рабочий класс для выполнения периодического обновления данных в фоновом режиме.
 *
 * @param context Контекст приложения.
 * @param params Параметры рабочего.
 */
class UpdateDataWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    // Создание экземпляра репозитория для доступа к данным задач
    private val repository: TodoItemsRepository by locateLazy()

    /**
    *
    *    Функция, которая выполняет работу рабочего исполнителя.
    *    Здесь происходит вызов метода getAllItemsFromBack репозитория для получения всех элементов задач с сервера.
    *    @return Результат работы рабочего исполнителя в виде объекта [Result].
    */
    override suspend fun doWork(): Result {
        try {
            repository.getAllItemsFromBack()
            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }

    companion object {
        private const val TAG = "UpdateDataWorker"

        /**
         * Функция для запуска периодической работы рабочего исполнителя.
         * Создается периодический запрос работы с заданными интервалом и ограничениями.
         * Запускается рабочий исполнитель с уникальным идентификатором.
         */
        fun enqueuePeriodicWork() {

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
            WorkManager.getInstance().enqueueUniquePeriodicWork(
                TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
        }
    }
}