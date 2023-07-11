package com.example.todoapp.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides

/**
 * Модуль Dagger, предоставляющий зависимости для работы с базой данных.
 */
@Module
class DBModule {
    /**
     * Метод для предоставления экземпляра базы данных `TodoItemsDatabase`.
     *
     * @param context Контекст приложения.
     * @return Экземпляр базы данных `TodoItemsDatabase`.
     */
    @Provides
    fun provideDatabase(context: Context): TodoItemsDatabase {
        return Room.databaseBuilder(
            context,
            TodoItemsDatabase::class.java,
            "todoItemsDatabase"
        ).build()
    }
}