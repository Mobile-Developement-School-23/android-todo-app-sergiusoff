package com.example.todoapp

import android.app.Application
import android.content.Context
import com.example.todoapp.model.TodoItemsRepository
import com.example.todoapp.database.TodoItemsDatabase
import com.example.todoapp.model.TodoItem
import com.example.todoapp.retrofit.ApiEntity
import com.example.todoapp.retrofit.ApiResponseSerializer
import com.example.todoapp.retrofit.TodoItemSerializer
import com.example.todoapp.retrofit.TodoItemsApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass

/**
 * Главный класс приложения.
 */
class App : Application() {

    /**
     * Вызывается при создании приложения.
     */
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.register<Context>(this)
        ServiceLocator.register(TodoItemsDatabase.create(locate()))

        val todoItemsApiService = Retrofit.Builder()
            .baseUrl("https://beta.mrdekk.ru/todobackend/")
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                .registerTypeAdapter(TodoItem::class.java, TodoItemSerializer())
                .registerTypeAdapter(ApiEntity::class.java, ApiResponseSerializer())
                .create()))
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val originalRequest = chain.request()
                val modifiedRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer agouara")
                    .build()
                chain.proceed(modifiedRequest)
            }.build())
            .build()
            .create(TodoItemsApiService::class.java)

        ServiceLocator.register(todoItemsApiService)
        ServiceLocator.register(TodoItemsRepository(locate(), locate()))
    }
}

object ServiceLocator{
    private val instances = mutableMapOf<KClass<*>, Any>()

    inline fun <reified T: Any> register(instance: T) = register(T::class, instance)

    fun <T: Any> register(KClass: KClass<T>, instance: T) {
        instances[KClass] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> get(KClass: KClass<T>): T = instances[KClass] as T
}

inline fun <reified T: Any> locate() = ServiceLocator.get(T::class)
inline fun <reified T: Any> locateLazy(): Lazy<T> = lazy { ServiceLocator.get(T::class) }