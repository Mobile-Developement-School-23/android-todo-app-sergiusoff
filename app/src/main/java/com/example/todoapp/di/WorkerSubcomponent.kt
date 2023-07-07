package com.example.todoapp.di

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.model.UpdateDataWorker
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Provider

@Subcomponent(modules = [UpdateDataWorker.Builder::class])
interface WorkerSubcomponent {

    fun workers(): Map<Class<out CoroutineWorker>, Provider<CoroutineWorker>>

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun workerParameters(param: WorkerParameters): Builder
        fun build(): WorkerSubcomponent
    }
}