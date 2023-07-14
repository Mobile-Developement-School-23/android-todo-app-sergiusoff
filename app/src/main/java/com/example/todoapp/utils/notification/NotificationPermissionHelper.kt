package com.example.todoapp.utils.notification

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class NotificationPermissionHelper {

    private val REQUEST_PERMISSION_NOTIFICATION = 1
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun showNotificationPermission(activity: Activity) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            activity, Manifest.permission.POST_NOTIFICATIONS
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            showExplanation(
                activity,
                "Уведомления отключены",
                "У данного приложение отсутствует разрешение на показ уведомлений.\n" +
                        "Для полноценной корректной работы приложения вам необходимо " +
                        "разрешить уведомления в настройках приложения.",
                Manifest.permission.POST_NOTIFICATIONS,
                REQUEST_PERMISSION_NOTIFICATION
            )
            requestPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS,
                REQUEST_PERMISSION_NOTIFICATION
            )
        } else {
            Log.d("Permission", "PERMISSION_GRANTED")
        }
    }

    fun showExplanation(
        context: Context,
        title: String,
        message: String,
        permission: String,
        permissionRequestCode: Int
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                requestPermission(context as Activity, permission, permissionRequestCode)
                dialog.dismiss()
            }
            .create()
            .show()
    }

    fun requestPermission(
        activity: Activity,
        permissionName: String,
        permissionRequestCode: Int
    ) {
        Log.d("Permission", "start requestPermission")
        ActivityCompat.requestPermissions(activity, arrayOf(permissionName), permissionRequestCode)
        Log.d("Permission", "end requestPermission")
    }
}