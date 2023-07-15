package com.example.todoapp.utils.notification

import android.app.Activity
import android.content.Context

interface NotificationPermissionHelpable {
    fun showNotificationPermission(activity: Activity)
    fun showExplanation(
        context: Context,
        title: String,
        message: String,
        permission: String,
        permissionRequestCode: Int
    )
    fun requestPermission(
        activity: Activity,
        permissionName: String,
        permissionRequestCode: Int
    )
}