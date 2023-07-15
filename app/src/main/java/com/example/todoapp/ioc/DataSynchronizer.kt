package com.example.todoapp.ioc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.di.AppScope
import com.example.todoapp.model.Event
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepository
import com.example.todoapp.retrofit.model.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Класс-прослойка между CreateEditViewModel и репозиторием
 * Необходим чтобы сохранить действия по добавлению/редактированию дела и отображение результата
 *
 * @param todoItemsRepository тот самый репозиторий, который ижектится откуда угодно
 */
@AppScope
class DataSynchronizer @Inject constructor(
    private val todoItemsRepository: TodoItemsRepository
){
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
// Подписка на LiveData как раз отвечает за показ изображений
    private val _todoItemProcess = MutableLiveData<Event<String>>()
    val todoItemProcess: LiveData<Event<String>> = _todoItemProcess

    /**
     * Устанавливает результат обработки операции с элементом списка дел.
     * @param result Результат операции в виде события.
     */
    fun setTodoItemProcess(result: Event<String>) {
        _todoItemProcess.postValue(result)
    }

    /**
     * Выполняет операцию сохранения элемента списка дел.
     * @param item Элемент списка дел для сохранения.
     */
    fun saveTodoItem(item: TodoItem) {
        if (item.text == ""){
            setTodoItemProcess(Event("Не удалось добавить дeло:\nнеобходим текст напоминания"))
            return
        }
        coroutineScope.launch {
            when (val result = todoItemsRepository.addItem(item)){
                is NetworkResult.Success -> setTodoItemProcess(Event("Данные успешно добавлены"))
                is NetworkResult.Error -> setTodoItemProcess(Event("Не удалось добавить данные: "
                        + result.errorMessage))
            }
        }
    }

    /**
     * Выполняет операцию изменения элемента списка дел.
     * @param item Элемент списка дел для изменения.
     */
    fun updateTodoItem(item: TodoItem) {
        coroutineScope.launch {
            when (val result = todoItemsRepository.updateItem(item)){
                is NetworkResult.Success -> setTodoItemProcess(Event("Данные успешно обновлены"))
                is NetworkResult.Error -> setTodoItemProcess(Event("Не удалось обновить данные: "
                        + result.errorMessage))
            }
        }
    }

    /**
     * Выполняет операцию удаления элемента списка дел.
     * @param item Элемент списка дел для удаления.
     */
    fun deleteTodoItem(item: TodoItem) {
        coroutineScope.launch {
            when (val result = todoItemsRepository.deleteItem(item)){
                is NetworkResult.Success -> setTodoItemProcess(Event("Данные успешно удалены"))
                is NetworkResult.Error -> setTodoItemProcess(Event("Не удалось удалить данные: " + result.errorMessage))
            }
        }
    }
}