package com.example.todoapp.model

import java.util.*

data class TodoItem(
    var id: String,
    var text: String,
    var importance: Importance,
    var deadline: Date?,
    var isDone: Boolean,
    var creationDate: Date,
    var lastModificationDate: Date?
)