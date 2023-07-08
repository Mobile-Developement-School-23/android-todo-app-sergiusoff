package com.example.todoapp.utils.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.todoapp.di.AppScope
import javax.inject.Inject
import javax.inject.Provider

/**
 * Фабрика Worker для использования с Dagger.
 *
 * @property workerSubcomponent Билдер подкомпонента Worker.
 */
@AppScope
class DaggerWorkerFactory @Inject constructor(
    private val workerSubcomponent: WorkerSubcomponent.Builder
) : WorkerFactory() {

    /**
     * Метод создания Worker с использованием Dagger.
     *
     * @param appContext Контекст приложения.
     * @param workerClassName Имя класса Worker.
     * @param workerParameters Параметры Worker.
     * @return Экземпляр ListenableWorker.
     */
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ) = workerSubcomponent
        .workerParameters(workerParameters)
        .build().run {
            createWorker(workerClassName, workers())
        }

    /**
     * Вспомогательная функция для создания экземпляра Worker.
     *
     * @param workerClassName Имя класса Worker.
     * @param workers Карта классов Worker и их провайдеров.
     * @return Экземпляр ListenableWorker.
     * @throws IllegalArgumentException Если не найдено связывание для указанного workerClassName.
     * @throws RuntimeException Если возникает ошибка при создании Worker.
     */
    private fun createWorker(

        workerClassName: String,
        workers: Map<Class<out CoroutineWorker>, Provider<CoroutineWorker>>
    ): ListenableWorker? = try {
        val workerClass = Class.forName(workerClassName).asSubclass(CoroutineWorker::class.java)
        var provider = workers[workerClass]
        if (provider == null) {
            // Поиск совместимого класса Worker и соответствующего провайдера
            for ((key, value) in workers) {
                if (workerClass.isAssignableFrom(key)) {
                    provider = value
                    break
                }
            }
        }
        if (provider == null) {
            throw IllegalArgumentException("Missing binding for $workerClassName")
        }

        provider.get()
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}