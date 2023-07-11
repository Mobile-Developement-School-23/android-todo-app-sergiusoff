package com.example.todoapp.di

import com.example.todoapp.model.TodoItemsRepository
import com.example.todoapp.model.TodoItemsRepositoryImpl
import dagger.Binds
import dagger.Module

/**
 * Модуль Dagger, предоставляющий привязку зависимостей для приложения.
 */
@Module
interface AppBindModule {
    /**
     * Метод для привязки `TodoItemsRepositoryImpl` к `TodoItemsRepository`.
     *
     * @param todoItemsRepositoryImpl Реализация `TodoItemsRepositoryImpl`.
     * @return Экземпляр `TodoItemsRepository`.
     */
    @Binds
    @AppScope
    fun bindTodoItemsRepository(
        todoItemsRepositoryImpl: TodoItemsRepositoryImpl
    ): TodoItemsRepository
}