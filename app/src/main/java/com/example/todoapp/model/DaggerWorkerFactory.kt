package com.example.todoapp.model

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.todoapp.di.AppScope
import com.example.todoapp.di.WorkerSubcomponent
import javax.inject.Inject
import javax.inject.Provider


@AppScope
class DaggerWorkerFactory @Inject constructor(
    private val workerSubcomponent: WorkerSubcomponent.Builder
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ) = workerSubcomponent
        .workerParameters(workerParameters)
        .build().run {
            Log.d("DaggerWorkerFactory", "createWorker")
            createWorker(workerClassName, workers())
        }

    private fun createWorker(

        workerClassName: String,
        workers: Map<Class<out CoroutineWorker>, Provider<CoroutineWorker>>
    ): ListenableWorker? = try {
        Log.d("DaggerWorkerFactory", workerClassName)
        val workerClass = Class.forName(workerClassName).asSubclass(CoroutineWorker::class.java)
        var provider = workers[workerClass]
        if (provider == null) {
            for ((key, value) in workers) {
                if (workerClass.isAssignableFrom(key)) {
                    provider = value
                    break
                }
            }
        }
        if (provider == null) {
            throw IllegalArgumentException("Missing binding for $workerClassName")
        }

        provider.get()
    } catch (e: Exception) {
        Log.d("DaggerWorkerFactory", e.toString())
        throw RuntimeException(e)
    }
}