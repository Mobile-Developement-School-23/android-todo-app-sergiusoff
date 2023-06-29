package com.example.todoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "TodoItems")
data class TodoItem(
    @PrimaryKey
    var id: UUID,
    var text: String,
    var importance: Importance,
    var deadline: Date?,
    var isDone: Boolean,
    var creationDate: Date,
    var lastModificationDate: Date
): Serializable