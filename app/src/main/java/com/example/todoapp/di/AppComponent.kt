package com.example.todoapp.di

import android.content.Context
import com.example.todoapp.database.DBModule
import com.example.todoapp.utils.worker.DaggerWorkerFactory
import com.example.todoapp.utils.worker.UpdateDataWorker
import com.example.todoapp.retrofit.NetworkModule
import com.example.todoapp.utils.changeNetworkState.NetworkSchedulerService
import com.example.todoapp.utils.worker.WorkerSubcomponent
import com.example.todoapp.ui.view.CreateEditFragment
import com.example.todoapp.ui.view.MainActivity
import com.example.todoapp.ui.view.TodoListFragment
import com.example.todoapp.utils.notification.NotificationComponent
import com.example.todoapp.utils.notification.NotificationModule
import dagger.BindsInstance
import dagger.Component

/**
 * Компонент Dagger, отвечающий за инъекцию зависимостей в приложение.
 */
@AppScope
@Component(modules = [DBModule::class, NetworkModule::class, AppBindModule::class, NotificationModule::class])
interface AppComponent {
    /**
     * Метод для инъекции зависимостей в `MainActivity`.
     */
    fun inject(mainActivity: MainActivity)

    /**
     * Метод для инъекции зависимостей в `CreateEditFragment`.
     */
    fun inject(createEditFragment: CreateEditFragment)

    /**
     * Метод для инъекции зависимостей в `TodoListFragment`.
     */
    fun inject(todoListFragment: TodoListFragment)

    /**
     * Метод для инъекции зависимостей в `NetworkSchedulerService`.
     */
    fun inject(networkSchedulerService: NetworkSchedulerService)

    /**
     * Метод для инъекции зависимостей в `UpdateDataWorker`.
     */
    fun inject(updateDataWorker: UpdateDataWorker)

    /**
     * Метод для создания `DaggerWorkerFactory` - кастомной фабрики для Worker.
     *
     * @return Экземпляр `DaggerWorkerFactory`.
     */
    fun daggerWorkerFactory(): DaggerWorkerFactory

    /**
     * Метод для получения билдера подкомпонента `WorkerSubcomponent`.
     *
     * @return Билдер для подкомпонента `WorkerSubcomponent`.
     */
    fun workerSubcomponentBuilder(): WorkerSubcomponent.Builder

    fun notificationComponent(): NotificationComponent


    /**
     * Билдер для компонента `AppComponent`.
     */
    @Component.Builder
    interface Builder {
        /**
         * Метод для установки контекста приложения.
         *
         * @param context Контекст приложения.
         * @return Экземпляр билдера.
         */
        @BindsInstance
        fun context(context: Context): Builder

        /**
         * Метод для создания экземпляра `AppComponent`.
         *
         * @return Экземпляр `AppComponent`.
         */
        fun build(): AppComponent
    }
}