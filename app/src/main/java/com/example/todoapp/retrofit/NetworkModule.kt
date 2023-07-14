package com.example.todoapp.retrofit

import com.example.todoapp.model.TodoItem
import com.example.todoapp.retrofit.model.ApiEntity
import com.example.todoapp.retrofit.model.ApiResponseSerializer
import com.example.todoapp.retrofit.model.TodoItemSerializer
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Модуль Dagger, предоставляющий зависимости для работы с сетью.
 */
@Module
class NetworkModule {
    /**
     * Метод для предоставления сервиса `TodoItemsApiService`.
     *
     * @return Экземпляр `TodoItemsApiService`.
     */
    @Provides
    fun provideTodoItemService(): TodoItemsApiService {
        return Retrofit.Builder()
            // Создание экземпляра Retrofit с базовым URL
            .baseUrl("https://beta.mrdekk.ru/todobackend/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        // Автоматичексий перевод TodoItem объектов в формат сервера и обратно
                        .registerTypeAdapter(TodoItem::class.java, TodoItemSerializer())
                        // Автоматичексий перевод request/response сервера в формат для кода и обратно
                        .registerTypeAdapter(ApiEntity::class.java, ApiResponseSerializer())
                        .create()
                )
            )
            .client(
                OkHttpClient.Builder().addInterceptor { chain ->
                    // Изменяем запрос, добавляя заголовок авторизации Bearer Token
                    val modifiedRequest = chain.request().newBuilder()
                        .header("Authorization", "Bearer agouara")
                        .build()
                    chain.proceed(modifiedRequest)
                }.build()
            )
            .build()
            .create(TodoItemsApiService::class.java)
    }
}