package com.example.todoapp.utils.worker

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Provider

/**
 * Подкомпонент Dagger, отвечающий за создание и предоставление различных типов Worker.
 * Необходим для того чтобы по красоте вызывать Inject в конструктор нашего UpdateDataWorker
 *
 * @property workers map, содержащий классы Worker в качестве ключей и провайдеры Worker в качестве значений.
 */
@Subcomponent(modules = [UpdateDataWorker.Builder::class])
interface WorkerSubcomponent {

    fun workers(): Map<Class<out CoroutineWorker>, Provider<CoroutineWorker>>

    /**
     * Билдер для подкомпонента `WorkerSubcomponent`.
     */
    @Subcomponent.Builder
    interface Builder {
        /**
         * Метод для привязки параметров Worker.
         *
         * @param param Параметры Worker.
         * @return Экземпляр билдера.
         */
        @BindsInstance
        fun workerParameters(param: WorkerParameters): Builder

        /**
         * Метод для создания экземпляра `WorkerSubcomponent`.
         *
         * @return Экземпляр `WorkerSubcomponent`.
         */
        fun build(): WorkerSubcomponent
    }
}