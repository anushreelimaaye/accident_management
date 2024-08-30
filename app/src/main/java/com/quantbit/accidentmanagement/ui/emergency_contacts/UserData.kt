package com.quantbit.accidentmanagement.ui.emergency_contacts

data class UserData(
    var user: String,
    val emergency_contacts: List<EmergencyContact>,
    val registered_devices: List<RegisteredDevice>
)


data class RegisteredDevice(
    val device_name: String
)

data class UserRequest(
    val user: String
)