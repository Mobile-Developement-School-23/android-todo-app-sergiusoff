package com.example.todoapp.retrofit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Приемник широковещательных сообщений для отслеживания изменений подключения к сети.
 *
 * @param listener Слушатель для обработки изменений подключения.
 */
class ConnectivityReceiver(private val listener: ConnectivityReceiverListener) : BroadcastReceiver() {

    /**
     * Вызывается при получении широковещательного сообщения.
     *
     * @param context Контекст приемника.
     * @param intent Полученное широковещательное сообщение.
     */
    override fun onReceive(context: Context, intent: Intent) {
        listener.onNetworkConnectionChanged(isConnected(context))
    }

    /**
     * Проверяет, подключено ли устройство к сети.
     *
     * @param context Контекст приемника.
     * @return true, если устройство подключено к сети; false в противном случае.
     */
    private fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    /**
     * Интерфейс слушателя для обработки изменений подключения к сети.
     */
    interface ConnectivityReceiverListener {
        /**
         * Вызывается при изменении подключения к сети.
         *
         * @param isConnected true, если устройство подключено к сети; false в противном случае.
         */
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }
}