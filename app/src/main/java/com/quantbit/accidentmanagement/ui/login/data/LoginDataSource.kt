package com.quantbit.accidentmanagement.ui.login.data

import android.util.Log
import com.quantbit.accidentmanagement.service.RetrofitClient
import com.quantbit.accidentmanagement.ui.login.data.model.LoggedInUser
import com.quantbit.accidentmanagement.ui.login.data.model.LoginRequest

import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private val authService = RetrofitClient.apiService

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            // Make the network request
            val response = authService.login(LoginRequest(username,password))

            // Check if the response is successful and contains the required data
            if (response != null && response.message.success) {
                val loggedInUser = response.message.data

                // Log the details
                Log.d("Login Data Source", "User: ${loggedInUser?.user}")
                Log.d("Login Data Source", "API Key: ${loggedInUser?.apiKey}")
                Log.d("Login Data Source", "API Secret: ${loggedInUser?.apiSecret}")

                // Return the success result with LoggedInUser
                Result.Success(loggedInUser!!)
            } else {
                // Handle empty or unsuccessful response
                Result.Error(IOException("Error logging in: empty or unsuccessful response"))
            }

        } catch (e: Throwable) {
            // Log and handle any exceptions
            Log.d("Login Data Source Error", e.message ?: "Unknown error")
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}