package com.example.todoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.locateLazy
import com.example.todoapp.model.Event
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepository
import com.example.todoapp.retrofit.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.util.Date

/**
 * ViewModel, отвечающая за управление списком задач.
 */
class TodoListViewModel : ViewModel() {
    // Поле, содержащее экземпляр репозитория для работы с задачами
    private val todoItemsRepository: TodoItemsRepository by locateLazy()

    // Поле, представляющее список задач в виде LiveDataFlow
    val todoItems = todoItemsRepository.getAll().asLiveDataFlow()

    // Функция-расширение для преобразования Flow в LiveDataFlow
    private fun <T> Flow<T>.asLiveDataFlow() = shareIn(CoroutineScope(Dispatchers.IO), SharingStarted.Eagerly, replay = 1)

    // Поле, содержащее MutableLiveData события отображения Snackbar
    private val _showSnackbarEvent = MutableLiveData<Event<String>>()

    // Публичное поле, представляющее LiveData события отображения Snackbar
    val showSnackbarEvent: LiveData<Event<String>> = _showSnackbarEvent

    // Функция для получения данных о задачах

    /**
     * Получает данные из удаленного источника и обновляет список задач.
     * В случае успеха отображает Snackbar с сообщением "Данные успешно загружены".
     * В случае ошибки отображает Snackbar с сообщением об ошибке.
     */
    fun fetchData() {
        // Запускаем корутину в viewModelScope, которая автоматически отменяется при выходе из ViewModel
        viewModelScope.launch {
            // Запрос данных из репозитория
            when (val result = todoItemsRepository.getAllItemsFromBack()) {
                is NetworkResult.Success -> {
                    // В случае успешного получения данных, очищаем и обновляем список задач и отображаем Snackbar
                    todoItemsRepository.clearAndInsertAllItems(result.data.list!!)
                    _showSnackbarEvent.value = Event("Данные успешно загружены")
                }
                is NetworkResult.Error -> {
                    val errorMessage = result.errorMessage
                    val exception = result.exception
                    // Обработка ошибки, например, отображение Snackbar с сообщением об ошибке
                    _showSnackbarEvent.value = Event("Данные появились только на телефоне:\n$errorMessage")
                }
            }
        }
    }

    /**
     * Обновляет задачу в удаленном источнике.
     * В случае успеха отображает Snackbar с сообщением "Клик! Статус инвертирован!".
     * В случае ошибки отображает Snackbar с сообщением об ошибке.
     */
    fun updateTodoItem(item: TodoItem) {
        // Запускаем корутину в viewModelScope, которая автоматически отменяется при выходе из ViewModel
        viewModelScope.launch {
            item.lastModificationDate = Date()
            when (val result = todoItemsRepository.updateItem(item)) {
                is NetworkResult.Success -> {
                    _showSnackbarEvent.value = Event("Клик! Статус инвертирован!")
                }
                is NetworkResult.Error -> {
                    val errorMessage = result.errorMessage
                    val exception = result.exception
                    // Обработка ошибки, например, отображение Snackbar с сообщением об ошибке
                    _showSnackbarEvent.value = Event("Почему-то не прошло на сервер:\n${errorMessage}")
                }
            }
        }
    }

    /**
     * Удаляет задачу из удаленного источника.
     * В случае успеха отображает Snackbar с сообщением "Ты-Дыщ! Успешно удалено!".
     * В случае ошибки отображает Snackbar с сообщением об ошибке.
     */
    // Функция для удаления задачи
    fun deleteTodoItem(item: TodoItem) {
        // Запускаем корутину в viewModelScope, которая автоматически отменяется при выходе из ViewModel
        viewModelScope.launch {
            when (val result = todoItemsRepository.deleteItem(item)) {
                is NetworkResult.Success -> {
                    _showSnackbarEvent.value = Event("Ты-Дыщ! Успешно удалено!")
                }
                is NetworkResult.Error -> {
                    val errorMessage = result.errorMessage
                    val exception = result.exception
                    _showSnackbarEvent.value = Event("Почему-то не стёрлось на сервере:\n${errorMessage}")
                }
            }
        }
    }
}