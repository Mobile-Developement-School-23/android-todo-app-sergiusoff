package com.example.todoapp.utils.worker

import androidx.work.CoroutineWorker
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * Аннотация-ключ для использования в качестве ключа в map'е Worker.
 *
 * @param value Класс CoroutineWorker.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class WorkerKey(val value: KClass<out CoroutineWorker>)