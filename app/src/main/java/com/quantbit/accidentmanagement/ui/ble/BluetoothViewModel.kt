package com.quantbit.accidentmanagement.ui.ble
import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*



class BluetoothViewModel(application: Application) : AndroidViewModel(application) {

    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val _discoveredDevices = MutableLiveData<List<BluetoothDevice>>()
    val discoveredDevices: LiveData<List<BluetoothDevice>> get() = _discoveredDevices

    private val discoveredDevicesList = mutableListOf<BluetoothDevice>()

    private val _connectionStatus = MutableLiveData<Map<BluetoothDevice, Boolean>>()
    val connectionStatus: LiveData<Map<BluetoothDevice, Boolean>> get() = _connectionStatus

    private val deviceConnectionMap = mutableMapOf<BluetoothDevice, Boolean>()

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action ?: return

            when (action) {
                BluetoothDevice.ACTION_FOUND -> {

                    // Check if the necessary permissions are granted
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_SCAN
                        ) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }


                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                    discoveredDevicesList.add(device)
                    _discoveredDevices.postValue(discoveredDevicesList)


                    Log.d("Bluetooth", "Discovered device: ${device.name} - ${device.address}")

                    Log.d("Bluetooth", "Discovered device: ${device.name} - ${device.address}")
                    val uuids = device.uuids

                    // Log or process the UUIDs
                    uuids?.forEach { uuid ->
                        Log.d("Bluetooth", "UUID: ${uuid.uuid}")
                    }

                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.d("Bluetooth", "Discovery finished.")
//                    _discoveredDevices.postValue(discoveredDevicesList)
//                    if (discoveredDevicesList.isNotEmpty()) {
//                        connectToDevice(discoveredDevicesList[0])
//                    }
                }
            }
        }
    }
    // Properly initialize MutableLiveData
    private val _pairedDevices = MutableLiveData<List<BluetoothDevice>>().apply {
        value = emptyList()
    }
    val pairedDevices: LiveData<List<BluetoothDevice>> get() = _pairedDevices


    init {
        val applicationContext = getApplication<Application>().applicationContext
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        applicationContext.registerReceiver(bluetoothReceiver, filter)

        val discoveryFinishedFilter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        applicationContext.registerReceiver(bluetoothReceiver, discoveryFinishedFilter)
        _connectionStatus.value = deviceConnectionMap
      //  loadPairedDevices()
    }





    private fun loadPairedDevices() {

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


            val devices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            if (devices != null) {
                _pairedDevices.value = devices.toList()
            } else {
                _pairedDevices.value = emptyList() // Handle the case where there are no bonded devices
            }



//        val devices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
//        _pairedDevices.value = devices?.toList() ?: emptyList()
    }

    fun startDiscovery() {
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }
        bluetoothAdapter.startDiscovery()
        Log.d("Bluetooth", "Starting discovery...")
    }

//    fun connectToDevice(device: BluetoothDevice) {
//        Thread {
//            if (ActivityCompat.checkSelfPermission(
//                    getApplication(),
//                    Manifest.permission.BLUETOOTH_SCAN
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                return@Thread
//            }
//
//            val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // SPP UUID, replace with your own
//
//            val socket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
//                device.createRfcommSocketToServiceRecord(uuid)
//            }
//
//            bluetoothAdapter?.cancelDiscovery()
//
//            try {
//                socket?.connect()
//                Log.d("Bluetooth", "Connected to ${device.name}")
//                _connectionStatus.postValue("Connected to ${device.name}")
//
//                // Now you can manage the streams
//                val inputStream = socket?.inputStream
//                val outputStream = socket?.outputStream
//
//                // Example of reading from the input stream
//                val buffer = ByteArray(1024)
//                val bytes = inputStream?.read(buffer)
//                val receivedMessage = String(buffer, 0, bytes ?: 0)
//                Log.d("Bluetooth", "Received message: $receivedMessage")
//
//                // Example of writing to the output stream
//                outputStream?.write("Hello, Bluetooth!".toByteArray())
//
//            } catch (e: IOException) {
//                Log.e("Bluetooth", "Could not connect to device", e)
//                _connectionStatus.postValue("Failed to connect to ${device.name}")
//                try {
//                    socket?.close()
//                } catch (closeException: IOException) {
//                    Log.e("Bluetooth", "Could not close socket", closeException)
//                }
//            }
//        }.start()
//    }

    fun connectToDevice(device: BluetoothDevice) {
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            // Check if the device is already connected
            val isAlreadyConnected = deviceConnectionMap[device] ?: false
            if (!isAlreadyConnected) {
                try {
                    val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                    // Attempt to connect to the device
                   // val uuid: UUID = device.uuids.firstOrNull()?.uuid ?: UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                    val socket = device.createRfcommSocketToServiceRecord(uuid)
                    socket.connect()
                    Log.d("Bluetooth", "Connected to ${device.name}")
                    inputStream = socket?.inputStream
                    outputStream = socket?.outputStream
                    startListeningForData()


                    // Update connection status
                    deviceConnectionMap[device] = true
                } catch (e: IOException) {
                    Log.e("Bluetooth", "Could not connect to device", e)
                    // Handle connection failure
                    deviceConnectionMap[device] = false
                } finally {

                    // Notify observers about the updated status
                    _connectionStatus.postValue(deviceConnectionMap)
                }
            }
        }
    }

    fun disconnectFromDevice(device: BluetoothDevice) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Assuming you have a way to get the socket associated with the device
                // socket?.close() - Example if you store sockets

                // Update connection status
                deviceConnectionMap[device] = false
            } catch (e: IOException) {
                // Handle disconnection failure
            } finally {
                // Notify observers about the updated status
                _connectionStatus.postValue(deviceConnectionMap)
            }
        }
    }

    fun updateConnectionStatus(device: BluetoothDevice, isConnected: Boolean) {
        deviceConnectionMap[device] = isConnected
        _connectionStatus.postValue(deviceConnectionMap)
    }
    private fun startListeningForData() {
        Thread {
            val buffer = ByteArray(1024)
            var bytes: Int

            try {
                while (true) {
                    bytes = inputStream?.read(buffer) ?: -1
                    if (bytes > 0) {
                        val data = String(buffer, 0, bytes)
                        Log.d("Bluetooth", "Received data: $data")
                        // Update UI or process data here
                    }
                }
            } catch (e: IOException) {
                Log.e("Bluetooth", "Error reading from InputStream", e)
            }
        }.start()
    }


    override fun onCleared() {
        super.onCleared()
        getApplication<Application>().applicationContext.unregisterReceiver(bluetoothReceiver)
    }
}
