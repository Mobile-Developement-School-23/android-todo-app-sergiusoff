package com.example.todoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.model.Event

/**
 * SharedViewModel используется для обмена данными и обратной связи между компонентами приложения.
 * Он содержит LiveData для обработки событий, связанных с процессом элементов списка дел.
 */
class SharedViewModel : ViewModel() {
    private val _todoItemProcess = MutableLiveData<Event<String>>()
    val todoItemProcess: LiveData<Event<String>> = _todoItemProcess

    /**
     * Устанавливает результат обработки операции с элементом списка дел.
     * @param result Результат операции в виде события.
     */
    fun setTodoItemProcess(result: Event<String>) {
        _todoItemProcess.postValue(result)
    }
}