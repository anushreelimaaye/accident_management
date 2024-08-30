package com.quantbit.accidentmanagement.ui.login.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val apiKey: String,
    val apiSecret: String,
    val user: String,
    val name:String
)

// Data class for the 'message' part of the response
data class Message(
    val success: Boolean,
    val message: String,
    val data: LoggedInUser?
)

// Data class for the complete API response
data class LoginResponse(
    val message: Message,
    val home_page: String,
    val full_name: String
)
data class LoginRequest(
    val usr: String,
    val pwd: String
)