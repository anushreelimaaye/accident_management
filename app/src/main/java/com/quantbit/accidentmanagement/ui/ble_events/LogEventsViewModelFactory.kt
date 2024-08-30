package com.quantbit.accidentmanagement.ui.ble_events

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quantbit.accidentmanagement.ui.emergency_contacts.list_contact.EmergencyContactsViewModel

class LogEventsViewModelFactory (private val repository: LogRepository, private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BLELogViewModel::class.java)) {
            return BLELogViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

