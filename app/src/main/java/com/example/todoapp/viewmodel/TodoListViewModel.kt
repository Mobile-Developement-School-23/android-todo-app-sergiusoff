package com.example.todoapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.locateLazy
import com.example.todoapp.model.Event
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepository
import com.example.todoapp.retrofit.ApiEntity
import com.example.todoapp.retrofit.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Date
import java.util.UUID

/**
 * ViewModel, отвечающая за управление списком задач.
 */
class TodoListViewModel : ViewModel() {
    private val todoItemsRepository: TodoItemsRepository by locateLazy()
    val todoItems = todoItemsRepository.getAll().asLiveDataFlow()
    private fun <T> Flow<T>.asLiveDataFlow() = shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

    private val _showSnackbarEvent = MutableLiveData<Event<String>>()
    val showSnackbarEvent: LiveData<Event<String>> = _showSnackbarEvent

    fun fetchData() {
        viewModelScope.launch {
            when (val result = todoItemsRepository.getAllItemsFromBack()) {
                is NetworkResult.Success -> {
                    todoItemsRepository.clearAndInsertAllItems(result.data.list!!)
                    _showSnackbarEvent.value = Event("Данные успешно загружены")
                }

                is NetworkResult.Error -> {
                    val errorMessage = result.errorMessage
                    val exception = result.exception
                    // Обработка ошибки, например, отображение Snackbar с сообщением об ошибке
                    _showSnackbarEvent.value = Event(errorMessage)
                }
            }
        }
    }

//    fun checkRetrofitWork() {
//        viewModelScope.launch {
//            todoItems.collect{
//                try {
//                    var response: ApiEntity? = null
//                response = todoItemsRepository.getAllItemsFromBack()
//                response = todoItemsRepository.updateAllItemsOnBack(7, ApiEntity("ok", null, it, 7))
//                response = todoItemsRepository.getItemByIdFromBack("faa1fa16-bc2c-4c22-94f9-fc829988926c")
//                response = todoItemsRepository.postItemOnBack(8, ApiEntity(null, TodoItem(UUID.randomUUID(), "KEKEW", Importance.IMPORTANT, Date(), true, Date(), Date()), null, null))
//                response = todoItemsRepository.putItemOnBack(13, "faa1fa16-bc2c-4c22-94f9-fc829988926c", ApiEntity(null, TodoItem(UUID.randomUUID(), "PUTTED_TEXT", Importance.IMPORTANT, Date(), true, Date(), Date()), null, null))
//                response = todoItemsRepository.deleteItemOnBack(15, "84b4a2f2-0faf-4f76-a618-c249f026266b")
//                Log.d("MainActivityty", "$response")
//                }
//                catch (e: Exception){
//                    Log.d("MainActivityty", e.toString())
//                }
//            }
//        }
//    }

    fun updateTodoItem(item: TodoItem) {
        viewModelScope.launch {
            item.lastModificationDate = Date()
            when (val result = todoItemsRepository.updateItem(item)) {
                is NetworkResult.Success -> {
                    _showSnackbarEvent.value = Event("Дело сделано!")
                }

                is NetworkResult.Error -> {
                    val errorMessage = result.errorMessage
                    val exception = result.exception
                    // Обработка ошибки, например, отображение Snackbar с сообщением об ошибке
                    _showSnackbarEvent.value = Event(errorMessage)
                }
            }
        }
    }

    fun deleteTodoItem(item: TodoItem) {
        viewModelScope.launch { todoItemsRepository.deleteItem(item) }
    }
}
