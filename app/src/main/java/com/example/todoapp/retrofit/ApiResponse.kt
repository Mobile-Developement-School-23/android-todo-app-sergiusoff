package com.example.todoapp.retrofit

import com.example.todoapp.model.TodoItem

data class ApiResponse (
    val status: String,
    val element: TodoItem,
    val revision: Int
)