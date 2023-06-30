package com.example.todoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.model.Event

class SharedViewModel : ViewModel() {
    private val _todoItemProcess = MutableLiveData<Event<String>>()
    val todoItemProcess: LiveData<Event<String>> = _todoItemProcess

    fun setTodoItemProcess(result: Event<String>) {
        _todoItemProcess.postValue(result)
    }
}