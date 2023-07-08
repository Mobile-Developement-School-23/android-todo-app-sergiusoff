package com.example.todoapp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.todoapp.di.AppComponent
import com.example.todoapp.di.DaggerAppComponent

/**
 * Главный класс приложения.
 */
class App : Application() {

    private var _appComponent: AppComponent? = null

    internal val appComponent: AppComponent
        get() = checkNotNull(_appComponent) {
            "AppComponent isn't initialized"
        }

    /**
     * Вызывается при создании приложения.
     */
    override fun onCreate() {
        super.onCreate()
        _appComponent = DaggerAppComponent.builder()
            .context(applicationContext)
            .build()

        WorkManager.initialize(this, Configuration.Builder().run {
            setWorkerFactory(appComponent.
            daggerWorkerFactory())
            build()
        })
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> applicationContext.appComponent
    }