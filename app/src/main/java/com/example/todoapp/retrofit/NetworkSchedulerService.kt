package com.example.todoapp.retrofit

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager

import android.util.Log
import com.example.todoapp.locateLazy
import com.example.todoapp.model.TodoItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("SpecifyJobSchedulerIdRange")
class NetworkSchedulerService : JobService(), ConnectivityReceiver.ConnectivityReceiverListener {

    @SuppressLint("SpecifyJobSchedulerIdRange")
    private val TAG = NetworkSchedulerService::class.java.simpleName
    private lateinit var mConnectivityReceiver: ConnectivityReceiver
    private val repository: TodoItemsRepository by locateLazy()

    @SuppressLint("SpecifyJobSchedulerIdRange")
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Service created")
        mConnectivityReceiver = ConnectivityReceiver(this)
    }

    @SuppressLint("SpecifyJobSchedulerIdRange")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        return START_NOT_STICKY
    }

    @SuppressLint("SpecifyJobSchedulerIdRange")
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(TAG, "onStartJob")
        registerReceiver(mConnectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.i(TAG, "onStopJob")
        unregisterReceiver(mConnectivityReceiver)
        return true
    }

    @SuppressLint("SpecifyJobSchedulerIdRange")
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        Log.i(TAG, "onNetworkConnectionChanged")
        if (isConnected) {
            Log.i(TAG, "isConnected")
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    Log.i(TAG, "try")
                    when (val response = repository.getAllItemsFromBack()) {
                        is NetworkResult.Success -> repository.clearAndInsertAllItems(response.data.list!!)
                        is NetworkResult.Error -> {}
                    }
                    Log.i(TAG, "ok")
                } catch (e: Exception) {
                    // Обработка ошибок
                }
            }
        }
    }
}
