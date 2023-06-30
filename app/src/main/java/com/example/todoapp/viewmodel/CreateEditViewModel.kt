package com.example.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.todoapp.locateLazy
import com.example.todoapp.model.Event
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepository
import com.example.todoapp.retrofit.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel для создания и редактирования элементов списка дел.
 * Основная задача этой ViewModel - сохранять, обновлять и удалять элементы списка дел через репозиторий.
 */
class CreateEditViewModel : ViewModel() {
    private val repository: TodoItemsRepository by locateLazy()
    /**
    * Использует sharedViewModel для обмена данными и обратной связи с другими компонентами.
    * Действия выполняются в фоновом режиме, не прерываются при выходе из активности или фрагмента.
    */
    private val myIOScope = CoroutineScope(Dispatchers.IO)
    lateinit var sharedViewModel: SharedViewModel

    /**
     * Сохраняет элемент списка дел.
     * Действие будет выполняться в течение жизни ViewModel.
     * @param todoItem Элемент списка дел для сохранения.
     */
    fun saveTodoItem(todoItem: TodoItem) {
        myIOScope.launch {
            when (val result = repository.addItem(todoItem)){
                is NetworkResult.Success -> sharedViewModel.setTodoItemProcess(Event("Данные успешно сохранены"))
                is NetworkResult.Error -> sharedViewModel.setTodoItemProcess(Event("Не удалось сохранить данные: " + result.errorMessage))
            }
        }
    }

    /**
     * Обновляет элемент списка дел.
     * Действие будет выполняться в течение жизни ViewModel.
     * @param todoItem Элемент списка дел для обновления.
     */
    fun updateTodoItem(todoItem: TodoItem) {
        myIOScope.launch {
            when (val result = repository.updateItem(todoItem)){
                is NetworkResult.Success -> sharedViewModel.setTodoItemProcess(Event("Данные успешно обновлены"))
                is NetworkResult.Error -> sharedViewModel.setTodoItemProcess(Event("Не удалось обновить данные: " + result.errorMessage))
            }
        }
    }

    /**
     * Удаляет элемент списка дел.
     * Действие будет выполняться в течение жизни ViewModel.
     * @param todoItem Элемент списка дел для удаления.
     */
    fun deleteTodoItem(todoItem: TodoItem) {
        myIOScope.launch {
            when (val result = repository.deleteItem(todoItem)){
                is NetworkResult.Success -> sharedViewModel.setTodoItemProcess(Event("Данные успешно удалены"))
                is NetworkResult.Error -> sharedViewModel.setTodoItemProcess(Event("Не удалось удалить данные: " + result.errorMessage))
            }
        }
    }
}