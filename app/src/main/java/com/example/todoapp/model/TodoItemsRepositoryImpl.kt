package com.example.todoapp.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.todoapp.database.TodoItemsDatabase
import com.example.todoapp.retrofit.model.ApiEntity
import com.example.todoapp.retrofit.model.NetworkResult
import com.example.todoapp.retrofit.TodoItemsApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject


/**
 * Репозиторий для управления списком задач [TodoItem].
 *
 * @property db Экземпляр базы данных для доступа к локальным данным.
 * @property todoItemsApiService Экземпляр API-сервиса для доступа к удаленным данным.
 */

class TodoItemsRepositoryImpl @Inject constructor(
    private val db: TodoItemsDatabase,
    private val todoItemsApiService: TodoItemsApiService,
    context: Context
) : TodoItemsRepository {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("todoItemApp", Context.MODE_PRIVATE)

    var revision: Int = 42
    private val dao get() = db.itemsDao

    /**
     * Возвращает все элементы списка задач из БД в виде `Flow`.
     *
     * @return `Flow` со списком элементов `TodoItem`.
     */
    override fun getAll(): Flow<List<TodoItem>> = dao.getAll()

    /**
     * Добавляет новый элемент списка задач в БД.
     *
     * @param item Новый элемент для добавления.
     */
    override suspend fun add(item: TodoItem) = dao.add(item)

    /**
     * Обновляет элемент списка задач в БД.
     *
     * @param item Элемент для обновления.
     */
    override suspend fun update(item: TodoItem) = dao.update(item)

    /**
     * Удаляет элемент списка задач в БД.
     *
     * @param item Элемент для удаления.
     */
    override suspend fun delete(item: TodoItem) = dao.delete(item)

    /**
     * Очищает таблицу элементов списка задач и вставляет новые элементы.
     *
     * @param items Список элементов для вставки.
     */
    override suspend fun clearAndInsertAllItems(items: List<TodoItem>) {
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
        var i = 0
        var result: NetworkResult<ApiEntity>
        // Защита от одновременного изменения с нескольких устройств
        // (запрос свежей ревизии в случае неудачи)
        do {
            result = try {
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
                try {
                    refreshRevision()
                    delay(100)
                } catch (_: java.lang.Exception) {
                }
                // Возвращаем результат выполнения запроса с ошибкой в виде объекта
                NetworkResult.Error(errorMessage, e)
                // Обработка исключения типа UnknownHostException, возникающего при проблемах с сетевым подключением
            } catch (e: UnknownHostException) {
                NetworkResult.Error(
                    "Кажется, какие-то проблемы с сетью\nИдёт работа с локальным хранилищем",
                    e
                )
            }
            // Обработка других исключений, которые могут возникнуть при выполнении запроса
            catch (e: Exception) {
                val errorMessage = "Неизвестная ошибка: ${e.message}"
                NetworkResult.Error(errorMessage, e)
            }
        } while (result !is NetworkResult.Success && i++ < 2)
        return result
    }

    /**
     * Получает все элементы списка задач из удаленного источника данных.
     *
     * @return Результат запроса в виде [NetworkResult].
     */
    override suspend fun getAllItemsFromBack(): NetworkResult<ApiEntity> {
        Log.d("Repository_CLASS", "getAllItemsFromBack\t$revision")
        return executeNetworkRequest { todoItemsApiService.getTodoItems() }
    }

    /**
     * Отправляет все элементы списка задач на удаленный сервер.
     *
     * @param newItems Список новых элементов для отправки.
     * @return Результат запроса в виде [NetworkResult].
     */
    override suspend fun postAllItemsOnBack(newItems: List<TodoItem>): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.updateTodoItems(revision,
            ApiEntity(null, null, newItems, revision)) }

    /**
     * Получает элемент списка задач по его идентификатору из удаленного источника данных.
     *
     * @param id Идентификатор элемента.
     * @return Результат запроса в виде [NetworkResult].
     */
    override suspend fun getItemByIdFromBack(id: String): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.getTodoItem(id) }

    /**
     * Отправляет новый элемент списка задач на удаленный сервер.
     *
     * @param newItem Новый элемент для отправки.
     * @return Результат запроса в виде [NetworkResult].
     */
    override suspend fun postItemOnBack(newItem: TodoItem): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.postTodoItem(revision, ApiEntity(null, newItem, null, null)) }

    /**
     * Обновляет элемент списка задач на удаленном сервере.
     *
     * @param item Элемент для обновления.
     * @return Результат запроса в виде [NetworkResult].
     */
    override suspend fun putItemOnBack(item: TodoItem): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.putTodoItem(revision, item.id.toString(),
            ApiEntity(null, item, null, null)) }

    /**
     * Удаляет элемент списка задач на удаленном сервере.
     *
     * @param id Идентификатор элемента для удаления.
     * @return Результат запроса в виде [NetworkResult].
     */
    override suspend fun deleteItemOnBack(id: String): NetworkResult<ApiEntity> =
        executeNetworkRequest { todoItemsApiService.deleteTodoItem(revision, id) }

    /**
     * Добавляет элемент списка задач и отправляет его на удаленный сервер.
     *
     * @param item Элемент для добавления.
     * @return Результат запроса в виде [NetworkResult].
     */
    override suspend fun addItem(item: TodoItem): NetworkResult<ApiEntity> {
        add(item)
        val result = postItemOnBack(item)
        if (result is NetworkResult.Error){
            sharedPreferences.edit().putBoolean("localChanged", true).apply()
        }
        return result
    }

    /**
     * Обновляет элемент списка задач и отправляет его на удаленный сервер.
     *
     * @param item Элемент для обновления.
     * @return Результат запроса в виде [NetworkResult].
     */
    override suspend fun updateItem(item: TodoItem): NetworkResult<ApiEntity> {
        update(item)
        val result = putItemOnBack(item)
        if (result is NetworkResult.Error){
            sharedPreferences.edit().putBoolean("localChanged", true).apply()
        }
        return result
    }

    /**
     * Удаляет элемент списка задач и отправляет запрос на удаление на удаленный сервер.
     *
     * @param item Элемент для удаления.
     * @return Результат запроса в виде [NetworkResult].
     */
    override suspend fun deleteItem(item: TodoItem): NetworkResult<ApiEntity> {
        delete(item)
        val result = deleteItemOnBack(item.id.toString())
        if (result is NetworkResult.Error){
            sharedPreferences.edit().putBoolean("localChanged", true).apply()
        }
        return result
    }

    /**
     * Обновляет значение переменной `revision` из удаленного источника данных.
     */
    override suspend fun refreshRevision() {
        CoroutineScope(Dispatchers.IO).launch {
            revision = when (val result = getAllItemsFromBack()) {
                is NetworkResult.Success -> result.data.revision!!
                else -> -1
            }
            Log.d("UPDATE_ITEM_REPO_IMPL_REVISION", "$revision")
        }
    }
}