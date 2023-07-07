package com.example.todoapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

import com.example.todoapp.model.Event
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepositoryImpl
import com.example.todoapp.retrofit.NetworkResult
import com.example.todoapp.utils.DataSynchronizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel для создания и редактирования элементов списка дел.
 * Основная задача этой ViewModel - сохранять, обновлять и удалять элементы списка дел через репозиторий.
 */
class CreateEditViewModel(
    private val todoItem: TodoItem,
    private val dataSynchronizer: DataSynchronizer
) : ViewModel() {

//    private val repository: TodoItemsRepositoryImpl by locateLazy()
    /**
    * Использует sharedViewModel для обмена данными и обратной связи с другими компонентами.
    * Действия выполняются в фоновом режиме, не прерываются при выходе из активности или фрагмента.
    */
    private val myIOScope = CoroutineScope(Dispatchers.IO)
//    lateinit var sharedViewModel: SharedViewModel


    /**
     * Сохраняет элемент списка дел.
     * Действие будет выполняться в течение жизни ViewModel.
     * @param todoItem Элемент списка дел для сохранения.
     */
    fun saveTodoItem(todoItem: TodoItem) {
        Log.d("ADD_ITEM", todoItem.toString())
        dataSynchronizer.saveTodoItem(todoItem)
//        myIOScope.launch {
//            when (val result = repository.addItem(todoItem)){
//                is NetworkResult.Success -> sharedViewModel.setTodoItemProcess(Event("Данные успешно сохранены"))
//                is NetworkResult.Error -> sharedViewModel.setTodoItemProcess(Event("Не удалось сохранить данные: " + result.errorMessage))
//            }
//        }
    }

    /**
     * Обновляет элемент списка дел.
     * Действие будет выполняться в течение жизни ViewModel.
     * @param todoItem Элемент списка дел для обновления.
     */
    fun updateTodoItem() {

        Log.d("UPDATE_ITEM", todoItem.toString())
        dataSynchronizer.updateTodoItem(todoItem)
//        myIOScope.launch {
//            when (val result = repository.updateItem(todoItem)){
//                is NetworkResult.Success -> sharedViewModel.setTodoItemProcess(Event("Данные успешно обновлены"))
//                is NetworkResult.Error -> sharedViewModel.setTodoItemProcess(Event("Не удалось обновить данные: " + result.errorMessage))
//            }
//        }
    }
//    fun updateTodoItem() {
//        Log.d("UPDATE_ITEM", todoItem.toString())
//        myIOScope.launch {
//            when (val result = repository.updateItem(todoItem)){
//                is NetworkResult.Success -> sharedViewModel.setTodoItemProcess(Event("Данные успешно обновлены"))
//                is NetworkResult.Error -> sharedViewModel.setTodoItemProcess(Event("Не удалось обновить данные: " + result.errorMessage))
//            }
//        }
//    }

    /**
     * Удаляет элемент списка дел.
     * Действие будет выполняться в течение жизни ViewModel.
     * @param todoItem Элемент списка дел для удаления.
     */
    fun deleteTodoItem(todoItem: TodoItem) {
        Log.d("UPDATE_ITEM", todoItem.toString())
        dataSynchronizer.deleteTodoItem(todoItem)
//        myIOScope.launch {
//            when (val result = repository.deleteItem(todoItem)){
//                is NetworkResult.Success -> sharedViewModel.setTodoItemProcess(Event("Данные успешно удалены"))
//                is NetworkResult.Error -> sharedViewModel.setTodoItemProcess(Event("Не удалось удалить данные: " + result.errorMessage))
//            }
//        }
    }
}