package com.quantbit.accidentmanagement.service

import com.quantbit.accidentmanagement.model.AuthInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import okhttp3.OkHttpClient


object RetrofitClient {

    private const val BASE_URL = "http://45.249.255.81:4419/" // Replace with your API base URL

    private val authInterceptor = AuthInterceptor() // Create an instance of AuthInterceptor

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // Add AuthInterceptor
            .build()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
           .client(createOkHttpClient()) // Add OkHttpClient with interceptor
            .addConverterFactory(GsonConverterFactory.create()) // Add Gson converter
            .build()
    }

    val apiService: ApiService by lazy {
        createRetrofit().create(ApiService::class.java)
    }

    fun setAuthToken(token: String) {
        authInterceptor.setToken(token) // Update the token in AuthInterceptor
    }
}
