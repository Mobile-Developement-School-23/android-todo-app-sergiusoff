package com.example.todoapp.model

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Рабочий класс для выполнения периодического обновления данных в фоновом режиме.
 *
 * @param context Контекст приложения.
 * @param params Параметры рабочего.
 */
class UpdateDataWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    @Inject
    lateinit var repository: TodoItemsRepository


    /**
    *
    *    Функция, которая выполняет работу рабочего исполнителя.
    *    Здесь происходит вызов метода getAllItemsFromBack репозитория для получения всех элементов задач с сервера.
    *    @return Результат работы рабочего исполнителя в виде объекта [Result].
    */
    override suspend fun doWork(): Result {
        Log.d("PERIODIC WORK", "Before try")
        try {
            Log.d("PERIODIC WORK", "Start try")
            repository.getAllItemsFromBack()
            Log.d("PERIODIC WORK", "Mid try")
            return Result.success()
        } catch (e: Exception) {
            Log.d("PERIODIC WORK", e.message.toString())
            return Result.retry()
        }
    }
}
//    companion object {
//
//        /**
//         * Функция для запуска периодической работы рабочего исполнителя.
//         * Создается периодический запрос работы с заданными интервалом и ограничениями.
//         * Запускается рабочий исполнитель с уникальным идентификатором.
//         */
//        fun enqueuePeriodicWork() {
//
//            // Определение ограничений для работы
//            val constraints = Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .setRequiresCharging(false)
//                .setRequiresBatteryNotLow(false)
//                .build()
//
//            // Создание периодического запроса работы
//            val periodicWorkRequest = PeriodicWorkRequestBuilder<UpdateDataWorker>(
//                repeatInterval = 1,
//                repeatIntervalTimeUnit = TimeUnit.MINUTES
//            )
//                .setConstraints(constraints)
//                .build()
//
//            // Запуск периодической работы рабочего исполнителя
//            WorkManager.getInstance().enqueueUniquePeriodicWork(
//                "UpdateDataWorker",
//                ExistingPeriodicWorkPolicy.KEEP,
//                periodicWorkRequest
//            )
//        }
//    }
