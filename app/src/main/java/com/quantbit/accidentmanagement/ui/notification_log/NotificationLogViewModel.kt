package com.quantbit.accidentmanagement.ui.notification_log

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quantbit.accidentmanagement.ui.ble_events.LogRepository

import com.quantbit.accidentmanagement.utility.SharedUtility
import kotlinx.coroutines.launch

class NotificationLogViewModel(private val repository: LogRepository, private val context: Context) : ViewModel() {

    val connectionData = MutableLiveData<Contact>()
    val _contactList = MutableLiveData<List<Contact>>()
    val contactList: LiveData<List<Contact>> get() = _contactList
    var user =""
    var token =""


    init {
        val sharedUtility = SharedUtility(context = context)
        user = sharedUtility.getString("user").toString()
        token=  sharedUtility.getString("token").toString()

        getNotificationLogs()
    }
    fun getNotificationLogs() {
        viewModelScope.launch {
            try {
                // Fetch the result from repository
                val result = repository.getNotificationLogs(user, token)

                // Handle the result
                when (result) {
                    is com.quantbit.accidentmanagement.ui.login.data.Result.Success -> {
                        _contactList.value = result.data.message.data
                        Log.d("EmergencyViewmodel",result.data.toString())
                    }
                    is com.quantbit.accidentmanagement.ui.login.data.Result.Error -> {
                        // Handle the error case
                        // _userData.value = ApiDataResponse("error","message",{]})  // Optionally set to null or handle it as needed
                        // Print or log the error message
                        print(result.exception.message)
                    }

                }
            } catch (e: Exception) {
                // Handle unexpected exceptions
                print(e.message)
            }
        }
    }

}