package com.example.todoapp.retrofit

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Интерфейс для взаимодействия с API списка задач.
 */
interface TodoItemsApiService {

    /**
     * Получает все элементы списка задач.
     */
    @GET("list")
    suspend fun getTodoItems(): ApiEntity

    /**
     * Обновляет все элементы списка задач.
     *
     * @param revision Последний известный номер ревизии.
     * @param request Тело запроса в виде [ApiEntity].
     */
    @PATCH("list")
    suspend fun updateTodoItems(@Header("X-Last-Known-Revision") revision: Int, @Body request: ApiEntity): ApiEntity

    /**
     * Получает элемент списка задач по указанному идентификатору.
     *
     * @param id Идентификатор элемента.
     */
    @GET("list/{id}")
    suspend fun getTodoItem(@Path("id") id: String): ApiEntity

    /**
     * Создает новый элемент списка задач.
     *
     * @param revision Последний известный номер ревизии.
     * @param request Тело запроса в виде [ApiEntity].
     */
    @POST("list")
    suspend fun postTodoItem(@Header("X-Last-Known-Revision") revision: Int, @Body request: ApiEntity): ApiEntity

    /**
     * Обновляет элемент списка задач с указанным идентификатором.
     *
     * @param revision Последний известный номер ревизии.
     * @param id Идентификатор элемента.
     * @param request Тело запроса в виде [ApiEntity].
     */
    @PUT("list/{id}")
    suspend fun putTodoItem(@Header("X-Last-Known-Revision") revision: Int, @Path("id") id: String, @Body request: ApiEntity): ApiEntity

    /**
     * Удаляет элемент списка задач с указанным идентификатором.
     *
     * @param revision Последний известный номер ревизии.
     * @param id Идентификатор элемента.
     */
    @DELETE("list/{id}")
    suspend fun deleteTodoItem(@Header("X-Last-Known-Revision") revision: Int, @Path("id") id: String): ApiEntity
}