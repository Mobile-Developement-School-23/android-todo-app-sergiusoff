package com.example.todoapp.utils.notification

import dagger.Subcomponent

@Subcomponent
interface NotificationComponent {
    fun inject(alarmReceiver: AlarmReceiver)
}