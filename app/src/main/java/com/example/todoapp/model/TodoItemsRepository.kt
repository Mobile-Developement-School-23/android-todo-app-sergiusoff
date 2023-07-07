package com.example.todoapp.model

import android.util.Log
import com.example.todoapp.database.TodoItemsDatabase
import com.example.todoapp.retrofit.ApiEntity
import com.example.todoapp.retrofit.NetworkResult
import com.example.todoapp.retrofit.TodoItemsApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException


/**
 * Репозиторий для управления списком задач [TodoItem].
 *
 * ToDo: разбить на несколько репозиториев (db, web)
 * @property db Экземпляр базы данных для доступа к локальным данным.
 * @property todoItemsApiService Экземпляр API-сервиса для доступа к удаленным данным.
 */
class TodoItemsRepository(
    private val db: TodoItemsDatabase,
    private val todoItemsApiService: TodoItemsApiService
) {

    var revision: Int = 21
    private val dao get() = db.itemsDao

    /**
     * Возвращает все элементы списка задач в виде `Flow`.
     */
    fun getAll(): Flow<List<TodoItem>> = dao.getAll()

    /**
     * Добавляет новый элемент списка задач.
     *
     * @param item Новый элемент для добавления.
     */
    suspend fun add(item: TodoItem) = dao.add(item)

    /**
     * Обновляет элемент списка задач.
     *
     * @param item Элемент для обновления.
     */
    suspend fun update(item: TodoItem) = dao.update(item)

    /**
     * Удаляет элемент списка задач.
     *
     * @param item Элемент для удаления.
     */
    suspend fun delete(item: TodoItem) = dao.delete(item)

    /**
     * Очищает таблицу элементов списка задач и вставляет новые элементы.
     *
     * @param items Список элементов для вставки.
     */
    suspend fun clearAndInsertAllItems(items: List<TodoItem>) {
        dao.deleteAll()
        dao.insertAll(items)
    }

    /**
     * Выполняет сетевой запрос и обрабатывает результат.
     *
     * @param apiCall Функция для выполнения сетевого запроса.
     * @return Результат сетевого запроса в виде [NetworkResult].
     */
    suspend inline fun executeNetworkRequest(crossinline apiCall: suspend () -> ApiEntity): NetworkResult<ApiEntity> {
        return try {
            // Выполняем сетевой запрос, вызывая переданную функцию apiCall
            val response = apiCall()
            // Обновляем значение переменной revision на основе значения response.revision
            revision = response.revision ?: revision
            // Возвращаем результат успешного выполнения запроса в виде объекта NetworkResult.Success
            NetworkResult.Success(response)
        } catch (e: HttpException) {
            // Обработка исключения типа HttpException, которое может возникнуть при выполнении запроса
            val errorMessage = when (e.code()) {
                400 -> "Неправильно сформирован запрос"
                401 -> "Неверная авторизация"
                404 -> "Такой элемент на сервере не найден"
                in 500..599 -> "Ошибка сервера"
                else -> "Неизвестная ошибка"
            }

            // Возвращаем результат выполнения запроса с ошибкой в виде объекта
            NetworkResult.Error(errorMessage, e)
            // Обработка исключения типа UnknownHostException, возникающего при проблемах с сетевым подключением
        } catch (e: UnknownHostException) {
            NetworkResult.Error("Кажется, какие-то проблемы с сетью\nИдёт работа с локальным хранилищем", e)
        }
        // Обработка других исключений, которые могут возникнуть при выполнении запроса
        catch (e: Exception) {
            val errorMessage = "Неизвестная ошибка: ${e.message}"
            NetworkResult.Error(errorMessage, e)
        }
    }

    /**
     * Получает все элементы списка задач из удаленного источника данных.
     *
     * @return Результат запроса в виде [NetworkResult].
     */
    suspend fun getAllItemsFromBack(): NetworkResult<ApiEntity> {
        Log.d("Repository_CLASS", "getAllItemsFromBack")
        return executeNetworkRequest { todoItemsApiService.getTodoItems() }
    }

    /**
     * Отправляет все элементы списка задач на удаленный сервер.
     *
     * @param newItems Список новых элементов для отправки.
     * @return Результат запроса в виде [NetworkResult].
     */
    suspend fun postAllItemsOnBack(newItems: List<TodoItem>): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.updateTodoItems(revision, ApiEntity(null, null, newItems, revision)) }

    /**
     * Получает элемент списка задач по его идентификатору из удаленного источника данных.
     *
     * @param id Идентификатор элемента.
     * @return Результат запроса в виде [NetworkResult].
     */
    suspend fun getItemByIdFromBack(id: String): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.getTodoItem(id) }

    /**
     * Отправляет новый элемент списка задач на удаленный сервер.
     *
     * @param newItem Новый элемент для отправки.
     * @return Результат запроса в виде [NetworkResult].
     */
    suspend fun postItemOnBack(newItem: TodoItem): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.postTodoItem(revision, ApiEntity(null, newItem, null, null)) }

    /**
     * Обновляет элемент списка задач на удаленном сервере.
     *
     * @param item Элемент для обновления.
     * @return Результат запроса в виде [NetworkResult].
     */
    suspend fun putItemOnBack(item: TodoItem): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.putTodoItem(revision, item.id.toString(), ApiEntity(null, item, null, null)) }

    /**
     * Удаляет элемент списка задач на удаленном сервере.
     *
     * @param id Идентификатор элемента для удаления.
     * @return Результат запроса в виде [NetworkResult].
     */
    suspend fun deleteItemOnBack(id: String): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.deleteTodoItem(revision, id) }

    /**
     * Добавляет элемент списка задач и отправляет его на удаленный сервер.
     *
     * @param item Элемент для добавления.
     * @return Результат запроса в виде [NetworkResult].
     */
    suspend fun addItem(item: TodoItem): NetworkResult<ApiEntity> {
        add(item)
        return postItemOnBack(item)
    }

    /**
     * Обновляет элемент списка задач и отправляет его на удаленный сервер.
     *
     * @param item Элемент для обновления.
     * @return Результат запроса в виде [NetworkResult].
     */
    suspend fun updateItem(item: TodoItem): NetworkResult<ApiEntity> {
        update(item)
        return putItemOnBack(item)
    }

    /**
     * Удаляет элемент списка задач и отправляет запрос на удаление на удаленный сервер.
     *
     * @param item Элемент для удаления.
     * @return Результат запроса в виде [NetworkResult].
     */
    suspend fun deleteItem(item: TodoItem): NetworkResult<ApiEntity> {
        delete(item)
        return deleteItemOnBack(item.id.toString())
    }

    /**
     * Обновляет значение переменной `revision` из удаленного источника данных.
     */
    suspend fun refreshRevision() {
        CoroutineScope(Dispatchers.IO).launch {
            revision = when (val result = getAllItemsFromBack()) {
                is NetworkResult.Success -> result.data.revision!!
                else -> -1
            }
        }
    }
}