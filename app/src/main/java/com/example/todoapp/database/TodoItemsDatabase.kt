package com.example.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoapp.model.TodoItem

/**
 * База данных для хранения списка задач.
 */
@Database(entities = [TodoItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class TodoItemsDatabase : RoomDatabase() {
    /**
     * Получает объект доступа к данным [TodoItemDao].
     */
    abstract val itemsDao: TodoItemDao

    companion object {
        /**
         * Создает экземпляр [TodoItemsDatabase] с использованием [Context].
         *
         * @param context Контекст приложения.
         * @return Экземпляр [TodoItemsDatabase].
         */
        fun create(context: Context): TodoItemsDatabase = Room.databaseBuilder(
            context,
            TodoItemsDatabase::class.java,
            "todoItemsDatabase"
        ).build()
    }
}