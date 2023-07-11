package com.example.todoapp.model

import com.example.todoapp.retrofit.model.ApiEntity
import com.example.todoapp.retrofit.model.NetworkResult
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс `TodoItemsRepository` определяет контракт для доступа к данным списка задач.
 *
 * Репозиторий предоставляет методы для работы с элементами списка задач, такие как получение всех элементов,
 * добавление, обновление и удаление элементов. Он является промежуточным слоем между источником данных (например,
 * базой данных или удаленным сервером) и другими компонентами приложения, такими как ViewModel или пользовательский
 * интерфейс.
 *
 * Разделение кода, отвечающего за доступ к данным, от кода, отвечающего за бизнес-логику и представление, позволяет
 * достичь более гибкой и модульной архитектуры приложения. Интерфейс `TodoItemsRepository` определяет общий
 * контракт, которому должны следовать все реализации репозитория, независимо от источника данных.
 *
 */
interface TodoItemsRepository {
    /**
     * Получает все элементы списка задач в виде `Flow`, обновляющийся при изменении данных.
     *
     * @return `Flow` со списком элементов `TodoItem`.
     */
    fun getAll(): Flow<List<TodoItem>>

    /**
     * Асинхронно добавляет новый элемент в список задач.
     *
     * @param item Новый элемент `TodoItem`.
     */
    suspend fun add(item: TodoItem)

    /**
     * Асинхронно обновляет существующий элемент в списке задач.
     *
     * @param item Существующий элемент `TodoItem`.
     */
    suspend fun update(item: TodoItem)

    /**
     * Асинхронно удаляет элемент из списка задач.
     *
     * @param item Элемент `TodoItem` для удаления.
     */
    suspend fun delete(item: TodoItem)

    /**
     * Асинхронно очищает и вставляет все элементы списка задач.
     *
     * @param items Список элементов `TodoItem` для вставки.
     */
    suspend fun clearAndInsertAllItems(items: List<TodoItem>)

    /**
     * Асинхронно получает все элементы списка задач с сервера.
     *
     * @return Результат операции сети `NetworkResult<ApiEntity>`.
     */
    suspend fun getAllItemsFromBack(): NetworkResult<ApiEntity>

    /**
     * Асинхронно отправляет все элементы списка задач на сервер.
     *
     * @param newItems Новый список элементов `TodoItem` для отправки.
     * @return Результат операции сети `NetworkResult<ApiEntity>`.
     */
    suspend fun postAllItemsOnBack(newItems: List<TodoItem>): NetworkResult<ApiEntity>

    /**
     * Асинхронно получает элемент списка задач по его идентификатору с сервера.
     *
     * @param id Идентификатор элемента.
     * @return Результат операции сети `NetworkResult<ApiEntity>`.
     */
    suspend fun getItemByIdFromBack(id: String): NetworkResult<ApiEntity>

    /**
     * Асинхронно отправляет новый элемент списка задач на сервер.
     *
     * @param newItem Новый элемент `TodoItem` для отправки.
     * @return Результат операции сети `NetworkResult<ApiEntity>`.
     */
    suspend fun postItemOnBack(newItem: TodoItem): NetworkResult<ApiEntity>

    /**
     * Асинхронно обновляет элемент списка задач на сервере.
     *
     * @param item Элемент `TodoItem` для обновления.
     * @return Результат операции сети `NetworkResult<ApiEntity>`.
     */
    suspend fun putItemOnBack(item: TodoItem): NetworkResult<ApiEntity>

    /**
     * Асинхронно удаляет элемент списка задач на сервере.
     *
     * @param id Идентификатор элемента для удаления.
     * @return Результат операции сети `NetworkResult<ApiEntity>`.
     */
    suspend fun deleteItemOnBack(id: String): NetworkResult<ApiEntity>

    /**
     * Асинхронно добавляет элемент списка задач на сервер.
     *
     * @param item Элемент `TodoItem` для добавления.
     * @return Результат операции сети `NetworkResult<ApiEntity>`.
     */
    suspend fun addItem(item: TodoItem): NetworkResult<ApiEntity>

    /**
     * Асинхронно обновляет элемент списка задач на сервере.
     *
     * @param item Элемент `TodoItem` для обновления.
     * @return Результат операции сети `NetworkResult<ApiEntity>`.
     */
    suspend fun updateItem(item: TodoItem): NetworkResult<ApiEntity>

    /**
     * Асинхронно удаляет элемент списка задач на сервере.
     *
     * @param item Элемент `TodoItem` для удаления.
     * @return Результат операции сети `NetworkResult<ApiEntity>`.
     */
    suspend fun deleteItem(item: TodoItem): NetworkResult<ApiEntity>

    /**
     * Асинхронно обновляет ревизию данных.
     */
    suspend fun refreshRevision()
}