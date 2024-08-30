package com.quantbit.accidentmanagement.ui.home

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.quantbit.accidentmanagement.ui.emergency_contacts.ContactRepository
import com.quantbit.accidentmanagement.utility.SharedUtility
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    // Properly initialize MutableLiveData
    private val _pairedDevices = MutableLiveData<List<BluetoothDevice>>().apply {
        value = emptyList()
    }
    val pairedDevices: LiveData<List<BluetoothDevice>> get() = _pairedDevices
    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    var user:String =""
    var token:String=""
    lateinit var sharedUtility:SharedUtility

    init {
       sharedUtility = SharedUtility(application.applicationContext)

        user = sharedUtility.getString("user").toString()
        token=  sharedUtility.getString("token").toString()
        loadPairedDevices()
        getContacts()
    }


     fun loadPairedDevices() {

        // Check if the necessary permissions are granted
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val devices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        if (devices != null) {
            _pairedDevices.value = devices.toList()
        } else {
            _pairedDevices.value = emptyList() // Handle the case where there are no bonded devices
        }

    }

    fun getContacts() {
        val repository = ContactRepository()
        viewModelScope.launch {
            try {
                // Fetch the result from repository
                val result = repository.getContacts(user,token)

                // Handle the result
                when (result) {
                    is com.quantbit.accidentmanagement.ui.login.data.Result.Success -> {
                        sharedUtility.saveEmergencyContacts(getApplication(),result.data.message.data.emergency_contacts)
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


    fun isDeviceAvailableForConnection(device: BluetoothDevice): Boolean {
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        // Check if the device is paired
        if (device.bondState != BluetoothDevice.BOND_BONDED) {
            Log.d("BleService", "Device is not paired")
            return false
        }

        // Attempt a lightweight connection to check availability
        return try {
            val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
            val socket = device.createRfcommSocketToServiceRecord(uuid)
            socket.connect()
            socket.close()
            true
        } catch (e: IOException) {
            Toast.makeText(getApplication(),"Bluetooth Device is not available for connection", Toast.LENGTH_SHORT).show()
            Log.d("BleService", "Device is not available for connection", e)
            false
        }
    }

}