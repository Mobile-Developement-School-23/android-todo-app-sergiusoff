package com.example.todoapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoapp.model.TodoItem

@Database(entities = [TodoItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class TodoItemsDatabase: RoomDatabase() {
    abstract val itemsDao: TodoItemDao

    companion object {
        fun create(context: Context) = Room.databaseBuilder(
            context,
            TodoItemsDatabase::class.java,
            "todoItemsDatabase"
        ).build()
    }
}