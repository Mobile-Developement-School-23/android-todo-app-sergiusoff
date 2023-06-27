package com.example.todoapp

import android.app.Application
import android.content.Context
import com.example.todoapp.model.TodoItemsRepository
import com.example.todoapp.model.database.TodoItemsDatabase
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
        ServiceLocator.register(TodoItemsRepository(locate()))
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