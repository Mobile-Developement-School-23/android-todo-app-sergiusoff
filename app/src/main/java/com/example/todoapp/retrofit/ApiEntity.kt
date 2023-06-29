package com.example.todoapp.retrofit

import com.example.todoapp.model.TodoItem

data class ApiEntity(
    val status: String?,
    val element: TodoItem?,
    val list: List<TodoItem>?,
    val revision: Int?
)