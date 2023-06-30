package com.example.todoapp.retrofit

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val errorMessage: String, val exception: Throwable) : NetworkResult<Nothing>()
}