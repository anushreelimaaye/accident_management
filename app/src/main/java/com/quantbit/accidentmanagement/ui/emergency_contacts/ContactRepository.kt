package com.quantbit.accidentmanagement.ui.emergency_contacts

import android.util.Log
import com.google.gson.Gson
import com.quantbit.accidentmanagement.model.ApiDataResponse
import com.quantbit.accidentmanagement.model.ApiResponse
import com.quantbit.accidentmanagement.service.RetrofitClient
import com.quantbit.accidentmanagement.ui.login.data.Result
import retrofit2.HttpException
import java.io.IOException

class ContactRepository {

    private val contactService = RetrofitClient.apiService

    suspend fun saveContacts(userData: UserData,token: String): Result<ApiResponse> {
        // Set the token in the RetrofitClient
        RetrofitClient.setAuthToken(token)
        return try {

            Log.d("Save Contacts",Gson().toJson(userData))
            Log.d("token",token)
            // Make the network request
            val response = contactService.saveContacts(userData)

            // Check if the response is successful
            if (response != null) {
                Log.d("token",token)
                Log.d("Save Contacts",Gson().toJson(response))

                Result.Success(response)

            } else {
                Log.d("Error in logging in ",Gson().toJson(response))
                Result.Error(IOException("Error logging in: empty response"))

            }
        } catch (e: HttpException) {
            Log.d("Http Exception",e.message())
            // Handle HTTP exceptions
            Result.Error(IOException("HTTP error saving contacts", e))
        } catch (e: IOException) {
            Log.d("IO Exception",e.message!!)
            // Handle network or IO exceptions
            Result.Error(IOException("Network error saving contacts", e))
        } catch (e: Exception) {
            Log.d("IO Exception",e.message!!)
            // Handle other unexpected exceptions
            Result.Error(IOException("Unexpected error saving contacts", e))
        }
    }

    suspend fun getContacts(user: String,token:String): Result<ApiDataResponse> {
        return try {

            // Make the network request
            val response = contactService.getContacts(user)

            // Check if the response is successful
            if (response != null) {
                print(response)
                Log.d("Response",Gson().toJson(response))
                Result.Success(response)
            } else {
                Result.Error(IOException("Error logging in: empty response"))
            }
        } catch (e: HttpException) {
            Log.d("Error HttpException",e.message())
            // Handle HTTP exceptions
            Result.Error(IOException("HTTP error saving contacts", e))
        } catch (e: IOException) {
            Log.d("Error IOException",e.message!!)
            // Handle network or IO exceptions
            Result.Error(IOException("Network error saving contacts", e))
        } catch (e: Exception) {
            Log.d("Error Exception",e.message!!)
            // Handle other unexpected exceptions
            Result.Error(IOException("Unexpected error saving contacts", e))
        }
    }

}