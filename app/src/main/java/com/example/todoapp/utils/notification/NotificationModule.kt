package com.example.todoapp.utils.notification

import dagger.Module
import dagger.Provides


@Module
class NotificationModule {

    @Provides
    fun provideNotificationHelper(): NotificationPermissionHelpable {
        return NotificationPermissionHelpableImpl()
    }
}