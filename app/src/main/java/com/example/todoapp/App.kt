package com.example.todoapp

import android.app.Application
import com.example.todoapp.model.TodoItemsRepository

/**
 * Главный класс приложения.
 */
class App : Application() {
    companion object {
        /**
         * Репозиторий для управления задачами.
         */
        lateinit var todoItemsRepository: TodoItemsRepository
    }

    /**
     * Вызывается при создании приложения.
     */
    override fun onCreate() {
        super.onCreate()
        todoItemsRepository = TodoItemsRepository()
    }
}
