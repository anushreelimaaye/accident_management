package com.quantbit.accidentmanagement.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.*

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.quantbit.accidentmanagement.R
import com.quantbit.accidentmanagement.ui.ble_events.LogRepository
import com.quantbit.accidentmanagement.ui.notification_log.Contact
import com.quantbit.accidentmanagement.utility.SharedUtility
import com.quantbit.accidentmanagement.utility.Utility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BleService : Service() {
    private var bluetoothSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private var isConnected: Boolean = false
    private lateinit var sharedUtility: SharedUtility


    companion object {
        private const val CHANNEL_ID = "BluetoothServiceChannel"
    }

    override fun onCreate() {
        super.onCreate()
        sharedUtility = SharedUtility(applicationContext)
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val deviceAddress = intent?.getStringExtra("device_address")
        val deviceName = intent?.getStringExtra("device_name")
        deviceAddress?.let {
            if(deviceAddress != null) {
                connectToDevice(it, deviceName!!)
            }else{
                Toast.makeText(applicationContext,"Sorry, not able to connect",Toast.LENGTH_SHORT).show()
            }
        }
        sharedUtility.saveString("device_name",deviceName!!)
        sharedUtility.saveString("device_address",deviceAddress!!)
        startForegroundService()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        closeConnection()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Bluetooth Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun startForegroundService() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Bluetooth Service")
            .setContentText("Managing Bluetooth Connection")
            .setSmallIcon(R.drawable.bluetooth)
            .build()

        startForeground(1, notification)
    }

//    private fun connectToDevice(deviceAddress: String) {
//        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//        val device = bluetoothAdapter.getRemoteDevice(deviceAddress)
//
//        Thread {
//            try {
//                if (ActivityCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.BLUETOOTH_CONNECT
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//
//                }
//                val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
//                bluetoothAdapter.cancelDiscovery()
//                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
//                bluetoothSocket?.connect()
//                isConnected = true
//                sharedUtility.saveString("status","Connected")
//                Toast.makeText(applicationContext,"Device is connected",Toast.LENGTH_SHORT).show()
//                inputStream = bluetoothSocket?.inputStream
//                outputStream = bluetoothSocket?.outputStream
//
//                // Start listening for data
//                listenForData()
//            } catch (e: IOException) {
//                e.printStackTrace()
//                sharedUtility.saveString("status","Disconnected")
//                Toast.makeText(applicationContext,"Device is disconnected or lost connection",Toast.LENGTH_SHORT).show()
//                isConnected = false
//                stopSelf()
//            }
//        }.start()
//    }

    private fun connectToDevice(deviceAddress: String,deviceName:String) {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }else{

        }
        CoroutineScope(Dispatchers.Main).launch {
          withContext(Dispatchers.IO) {
                try {
                    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device = bluetoothAdapter.getRemoteDevice(deviceAddress)
                    val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                    bluetoothAdapter.cancelDiscovery()
                    bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
                    bluetoothSocket?.connect()
                    sendConnectionStatus("Connected",deviceName,deviceAddress,"Device Connected")//
                    isConnected = true
                    inputStream = bluetoothSocket?.inputStream
                    outputStream = bluetoothSocket?.outputStream
                    sharedUtility.saveString("status", "Connected")
                    listenForData(deviceName,deviceAddress)
                    "Connected"
                } catch (e: IOException) {
                    e.printStackTrace()
                    sharedUtility.saveString("status", "Disconnected")
                    isConnected = false
                    stopSelf()
                    sendConnectionStatus("Disconnected",deviceName,deviceAddress,"Device Disconnected")
                    "Disconnected"
                }
            }

        }
    }


    private fun sendConnectionStatus(status: String,deviceName: String,deviceAddress: String,event :String) {
        val logRepository = LogRepository()
        val user =sharedUtility.getString("user")
        val token =sharedUtility.getString("token")

        val batteryStatus= Utility.getBatteryPercentage(applicationContext)
        val time = System.currentTimeMillis()
        if(user !=null && token!=null){
            CoroutineScope(Dispatchers.IO).launch {
                try {

                    logRepository.saveDeviceLogs(user,token,deviceName,deviceAddress,status,batteryStatus.toString(),time.toString(),event)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun listenForData(deviceName: String, deviceAddress: String) {
        val buffer = ByteArray(1024) // Adjust buffer size as needed
        var bytes: Int

        CoroutineScope(Dispatchers.IO).launch {
            while (isConnected) {
                try {
                    // Read data into the buffer
                    bytes = inputStream?.read(buffer) ?: -1

                    if (bytes > 0) {
                        val receivedData = String(buffer, 0, bytes).trim() // Trim any excess whitespace
                        Log.d("BluetoothService", "Received: $receivedData")

                        // Check for specific data and process accordingly
                        if (receivedData == "GPIO Interrupt Triggered") {
                            withContext(Dispatchers.Main) {
                                sendNotification(receivedData)
                                getCurrentLocationAndSendSms()
                            }
                        }

                        // Optionally, you can handle other types of data here

                        // Send connection status
                        sendConnectionStatus("Connected", deviceName, deviceAddress, "Event Triggered")
                    } else if (bytes == -1) {
                        // End of stream
                        handleDisconnection()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    sendConnectionStatus("Disconnected", deviceName, deviceAddress, "Device disconnected")
                    handleDisconnection()
                }
            }
        }
    }


//    private fun listenForData(deviceName: String,deviceAddress: String) {
//        val buffer = ByteArray(1024)
//        var bytes: Int
//
//        while (isConnected) {
//            try {
//                bytes = inputStream?.read(buffer) ?: -1
//                if (bytes > 0) {
//                    val receivedData = String(buffer, 0, bytes)
//                    Log.d("BluetoothService", "Received: $receivedData")
//
//                    // Send notification with received data
//                    if(receivedData == "GPIO Interrupt Triggered") {
//                        sendNotification(receivedData)
//                        getCurrentLocationAndSendSms()
//                    }
//                    sendConnectionStatus("Connected",deviceName,deviceAddress,"Event Triggered")
//
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//
//                sendConnectionStatus("Disconnected",deviceName,deviceAddress,"Device disconnected")
//                handleDisconnection()
//            }
//        }
//    }

    private fun sendNotification(data: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Data Received")
            .setContentText("Received: $data")
            .setSmallIcon(androidx.core.R.drawable.notification_bg)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getCurrentLocationAndSendSms() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        val contactList = mutableListOf<Contact>()

                        // Retrieve the list of emergency contacts from SharedPreferences
                        val contacts = sharedUtility.getEmergencyContacts(this)

                        // Send the SMS to each contact
                        contacts.forEach { contact ->
                            val message = "Emergency Alert!\n" + "Hello ${contact.contact_name},"+ "I need help.\n My current location is: https://maps.google.com/?q=$latitude,$longitude"

                            contactList.add(Contact(contact.contact_name!!,contact.phone!!,message))
                            sendSMS(contact.contact_name!!,contact.phone!!, message)
                        } ?: run {
                            Toast.makeText(this, "No emergency contacts found", Toast.LENGTH_SHORT).show()
                        }
                        sendNotificationStatus(contactList.toList())
                    } else {
                        Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }


    fun sendSMS(name:String,phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(applicationContext, "SMS Sent!", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "SMS Failed to Send, Please try again", Toast.LENGTH_LONG).show()
        }


    }

    private fun sendNotificationStatus(contactList: List<Contact>) {
        val logRepository = LogRepository()
        val user =sharedUtility.getString("user")
        val token =sharedUtility.getString("token")

        if(user !=null && token!=null){
            CoroutineScope(Dispatchers.IO).launch {
                try {

                    logRepository.saveNotificationLogs(user,
                        token,contactList)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }


    private fun handleDisconnection() {
        isConnected = false
        closeConnection()
        stopSelf()
    }

    private fun closeConnection() {
        try {
            bluetoothSocket?.close()
            inputStream?.close()
            outputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
