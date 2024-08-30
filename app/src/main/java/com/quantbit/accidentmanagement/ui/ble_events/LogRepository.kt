package com.quantbit.accidentmanagement.ui.ble_events

import android.util.Log
import com.google.gson.Gson
import com.quantbit.accidentmanagement.model.ApiResponse
import com.quantbit.accidentmanagement.service.RetrofitClient
import com.quantbit.accidentmanagement.ui.login.data.Result
import com.quantbit.accidentmanagement.ui.notification_log.Contact
import com.quantbit.accidentmanagement.ui.notification_log.NotificationLogRequest
import com.quantbit.accidentmanagement.ui.notification_log.NotificationLogResponse
import retrofit2.HttpException
import java.io.IOException


class LogRepository {
    private val bleLogService = RetrofitClient.apiService

    suspend fun getDeviceLogs(user: String,token:String,deviceName: String,deviceAddress: String): Result<BLEResponse> {
        RetrofitClient.setAuthToken(token)
        return try {
            val blelogRequest = BLELogRequest(deviceName,deviceAddress)
            Log.d("Device logs", Gson().toJson(blelogRequest))
            // Make the network request
            val response = bleLogService.getDeviceLogs(blelogRequest)

            // Check if the response is successful
            if (response != null) {
                print(response)
                Log.d("Response", Gson().toJson(response))
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

    suspend fun saveDeviceLogs(user: String,token:String,deviceName:String,deviceAddress:String,status:String, batteryStatus:String,time: String,event:String): Result<ApiResponse> {
        RetrofitClient.setAuthToken(token)
        return try {
              val dailyLog= listOf(DailyLog("",time,status, time,batteryStatus, event ))
            // Make the network request
            val bleLogRequest = AddBleLogRequest(user,deviceName,deviceAddress,time,status, batteryStatus,dailyLog)
            Log.d("Save connection status", Gson().toJson(bleLogRequest))
            val response = bleLogService.insertLogs(bleLogRequest)

            // Check if the response is successful
            if (response != null) {
                print(response)
                Log.d("Response", Gson().toJson(response))
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


    suspend fun saveNotificationLogs(user: String,token:String, contactList:List<Contact>): Result<ApiResponse> {
        RetrofitClient.setAuthToken(token)
        return try {
            val bleLogRequest = NotificationLogRequest(user,contactList)

            Log.d("Save connection status", Gson().toJson(bleLogRequest))
            val response = bleLogService.addNotificationLog(bleLogRequest)

            // Check if the response is successful
            if (response != null) {
                print(response)
                Log.d("Response", Gson().toJson(response))
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

    suspend fun getNotificationLogs(user: String,token:String): Result<NotificationLogResponse> {
        RetrofitClient.setAuthToken(token)
        return try {

            // Make the network request
            val response = bleLogService.getNotificationLog(user)

            // Check if the response is successful
            if (response != null) {
                print(response)
                Log.d("Response", Gson().toJson(response))
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