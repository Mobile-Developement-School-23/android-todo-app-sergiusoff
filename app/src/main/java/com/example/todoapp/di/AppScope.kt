package com.example.todoapp.di

import javax.inject.Scope

/**
 * Scope Dagger, для всего жизненного цикла приложения.
 * Singleton же рекомендовали не использовать, так что вот
 */
@Scope
annotation class AppScope