package com.example.todoapp.retrofit

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoItemsApiService {
    @GET("list")
    suspend fun getTodoItems(): ApiEntity

    @PATCH("list")
    suspend fun updateTodoItems(@Header("X-Last-Known-Revision") revision: Int, @Body request: ApiEntity): ApiEntity

    @GET("list/{id}")
    suspend fun getTodoItem(@Path("id") id: String): ApiEntity

    @POST("list")
    suspend fun postTodoItem(@Header("X-Last-Known-Revision") revision: Int, @Body request: ApiEntity): ApiEntity

    @PUT("list/{id}")
    suspend fun putTodoItem(@Header("X-Last-Known-Revision") revision: Int, @Path("id") id: String, @Body request: ApiEntity): ApiEntity

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(@Header("X-Last-Known-Revision") revision: Int, @Path("id") id: String): ApiEntity
}