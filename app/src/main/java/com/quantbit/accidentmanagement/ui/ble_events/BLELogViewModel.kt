package com.quantbit.accidentmanagement.ui.ble_events

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quantbit.accidentmanagement.utility.SharedUtility
import kotlinx.coroutines.launch

class BLELogViewModel(private val repository:LogRepository,private val context: Context) : ViewModel() {
    val connectionData = MutableLiveData<MessageDetails>()
    val lastConnectionStatus = MutableLiveData<String>()
    val lastBluetoothStatus= MutableLiveData<String>()
    val lastConnectionTime= MutableLiveData<String>()

    private val _deviceName = MutableLiveData<List<String>>()
    private val _deviceAddress = MutableLiveData<List<String>>()
    //val deviceName: LiveData<List<String>> get() = _deviceName

    var user:String =""
    var token:String=""
    var device:String =""
    var address:String=""

    init {
        val sharedUtility = SharedUtility(context = context)
        user = sharedUtility.getString("user").toString()
        token=  sharedUtility.getString("token").toString()
        device = sharedUtility.getString("device_name").toString()
        address=  sharedUtility.getString("device_address").toString()

        getBLELogs()
    }
    fun getBLELogs() {
        viewModelScope.launch {
            try {
                // Fetch the result from repository
                val result = repository.getDeviceLogs(user, token,device,address)

                // Handle the result
                when (result) {
                    is com.quantbit.accidentmanagement.ui.login.data.Result.Success -> {
                        // Set the value of _userData with the data from Result.Success
                        connectionData.value = result.data.message.message
                        lastConnectionStatus.value = result.data.message.message.connection_status
                        lastBluetoothStatus.value = result.data.message.message.battery_status
                        lastConnectionTime.value = result.data.message.message.last_connection_time
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

