package com.quantbit.accidentmanagement.service

import com.quantbit.accidentmanagement.model.ApiDataResponse
import com.quantbit.accidentmanagement.model.ApiResponse
import com.quantbit.accidentmanagement.ui.ble_events.AddBleLogRequest
import com.quantbit.accidentmanagement.ui.ble_events.BLELogRequest
import com.quantbit.accidentmanagement.ui.ble_events.BLEResponse
import com.quantbit.accidentmanagement.ui.emergency_contacts.UserData
import com.quantbit.accidentmanagement.ui.login.data.model.LoginRequest
import com.quantbit.accidentmanagement.ui.login.data.model.LoginResponse
import com.quantbit.accidentmanagement.ui.notification_log.NotificationLogRequest
import com.quantbit.accidentmanagement.ui.notification_log.NotificationLogResponse

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("api/method/accident_management.accident_management.apis.app.login") // Define the HTTP method and endpoint
     suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("api/method/accident_management.accident_management.apis.contact.get_customer_data") // Define the HTTP method and endpoint
    suspend fun getContacts(@Query("user") username: String): ApiDataResponse

    @Headers("Content-Type: application/json")
    @POST("api/method/accident_management.accident_management.apis.contact.add_or_update_customer") // Define the HTTP method and endpoint
    suspend fun saveContacts(@Body userData: UserData): ApiResponse

    @POST("api/method/accident_management.accident_management.apis.ble.get_daily_ble_logs") // Define the HTTP method and endpoint
    suspend fun getDeviceLogs(@Body bleRequest: BLELogRequest): BLEResponse

    @POST("api/method/accident_management.accident_management.apis.ble.update_ble_device_info") // Define the HTTP method and endpoint
    suspend fun insertLogs(@Body bleRequest: AddBleLogRequest): ApiResponse

    @POST("api/method/accident_management.accident_management.apis.ble.notification_event_log") // Define the HTTP method and endpoint
    suspend fun addNotificationLog(@Body bleRequest: NotificationLogRequest): ApiResponse

    @POST("api/method/accident_management.accident_management.apis.ble.get_notification_event_logs") // Define the HTTP method and endpoint
    suspend fun getNotificationLog(@Query("user") username: String): NotificationLogResponse


}