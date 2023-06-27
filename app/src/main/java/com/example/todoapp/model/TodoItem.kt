package com.example.todoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "TodoItems")
data class TodoItem(
    @PrimaryKey
    var id: String,
    var text: String,
    var importance: Importance,
    var deadline: Date?,
    var isDone: Boolean,
    var creationDate: Date,
    var lastModificationDate: Date?
)