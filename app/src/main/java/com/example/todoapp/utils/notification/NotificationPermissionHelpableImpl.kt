package com.example.todoapp.utils.notification

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class NotificationPermissionHelpableImpl : NotificationPermissionHelpable {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun showNotificationPermission(activity: Activity) {
        Log.d("PermissionImpl", "showNotificationPermission")
        val notificationHelper = NotificationPermissionHelper()
        notificationHelper.showNotificationPermission(activity)
    }

    override fun showExplanation(
        context: Context,
        title: String,
        message: String,
        permission: String,
        permissionRequestCode: Int
    ) {
        val notificationHelper = NotificationPermissionHelper()
        notificationHelper.showExplanation(
            context,
            title,
            message,
            permission,
            permissionRequestCode
        )
    }

    override fun requestPermission(
        activity: Activity,
        permissionName: String,
        permissionRequestCode: Int
    ) {
        val notificationHelper = NotificationPermissionHelper()
        notificationHelper.requestPermission(activity, permissionName, permissionRequestCode)
    }
}