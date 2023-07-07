package com.example.todoapp

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.todoapp.model.TodoItemsRepository
import com.example.todoapp.database.TodoItemsDatabase
import com.example.todoapp.model.TodoItem
import com.example.todoapp.retrofit.ApiEntity
import com.example.todoapp.retrofit.ApiResponseSerializer
import com.example.todoapp.retrofit.TodoItemSerializer
import com.example.todoapp.retrofit.TodoItemsApiService
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

        // Регистрация зависимостей в ServiceLocator
        ServiceLocator.register<Context>(this)
        ServiceLocator.register(TodoItemsDatabase.create(locate()))

        // Создание экземпляра сервиса TodoItemsApiService с использованием Retrofit
        val todoItemsApiService = createRetrofitService("YOUR_TOKEN_SHOULD_BE_THERE")
        ServiceLocator.register(todoItemsApiService)

        // Создание репозитория TodoItemsRepository и запуск обновления ревизии в фоновом режиме
        val tdr = TodoItemsRepository(locate(), locate())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                tdr.refreshRevision()
            } catch (e: Exception) {
                // Обработка ошибки обновления ревизии (пустой блок catch)
            }
        }
        ServiceLocator.register(tdr)
    }

    /**
     * Создает экземпляр TodoItemsApiService с использованием Retrofit.
     */
    private fun createRetrofitService(token: String) =
        Retrofit.Builder()
            .baseUrl("https://beta.mrdekk.ru/todobackend/")
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(TodoItem::class.java, TodoItemSerializer())
                    .registerTypeAdapter(ApiEntity::class.java, ApiResponseSerializer())
                    .create()))
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val originalRequest = chain.request()
                val modifiedRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
                chain.proceed(modifiedRequest)
            }.build())
            .build()
            .create(TodoItemsApiService::class.java)

}

/**
 * Объект ServiceLocator, который хранит экземпляры зависимостей.
 */
object ServiceLocator {
    private val instances = mutableMapOf<KClass<*>, Any>()

    /**
     * Регистрирует экземпляр зависимости в ServiceLocator.
     */
    inline fun <reified T : Any> register(instance: T) = register(T::class, instance)

    /**
     * Также регистрирует экземпляр зависимости в ServiceLocator (в одну строку не получилось так красиво)
     */
    fun <T : Any> register(KClass: KClass<T>, instance: T) {
        instances[KClass] = instance
    }

    /**
     * Получает экземпляр зависимости из ServiceLocator.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(KClass: KClass<T>): T = instances[KClass] as T
}

/**
 * Возвращает экземпляр зависимости из ServiceLocator по ее классу.
 */
inline fun <reified T : Any> locate() = ServiceLocator.get(T::class)

/**
 * Возвращает ленивую инициализацию экземпляра зависимости из ServiceLocator по ее классу.
 */
inline fun <reified T : Any> locateLazy(): Lazy<T> = lazy { ServiceLocator.get(T::class) }