package com.quantbit.accidentmanagement.ui.notification_log

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quantbit.accidentmanagement.ui.ble_events.LogRepository



class NotificationViewModelFactory (private val repository: LogRepository, private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationLogViewModel::class.java)) {
            return NotificationLogViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}