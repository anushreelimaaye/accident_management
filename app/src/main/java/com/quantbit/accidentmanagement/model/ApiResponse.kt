package com.quantbit.accidentmanagement.model

import com.quantbit.accidentmanagement.ui.emergency_contacts.EmergencyContact
import com.quantbit.accidentmanagement.ui.emergency_contacts.UserData

data class ApiResponse(
    val message: Message
) {
    data class Message(
        val status: String,
        val message: String
    )
}

// Data class for the inner message object
data class Message(
    val status: String,
    val message: String,
    val data: UserData
)

// Data class for the main response
data class ApiDataResponse(
    val message: Message
)

// Data class for the user data
data class UserData(
    val user: String,
    val emergency_contacts: List<EmergencyContact>,
    val registered_devices: List<Device>
)


// Data class for device information
data class Device(
    val device_name: String
)