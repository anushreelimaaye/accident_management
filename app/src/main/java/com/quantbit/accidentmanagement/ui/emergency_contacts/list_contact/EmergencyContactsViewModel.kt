package com.quantbit.accidentmanagement.ui.emergency_contacts.list_contact

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quantbit.accidentmanagement.model.ApiDataResponse
import com.quantbit.accidentmanagement.model.ApiResponse
import com.quantbit.accidentmanagement.ui.emergency_contacts.ContactRepository
import com.quantbit.accidentmanagement.ui.emergency_contacts.EmergencyContact
import com.quantbit.accidentmanagement.ui.emergency_contacts.RegisteredDevice
import com.quantbit.accidentmanagement.ui.emergency_contacts.UserData
import com.quantbit.accidentmanagement.utility.SharedUtility
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class EmergencyContactsViewModel(private val repository: ContactRepository,private val context: Context) : ViewModel() {
    val contact = MutableLiveData(EmergencyContact("", "", "", ""))
    private val _result = MutableLiveData<com.quantbit.accidentmanagement.ui.login.data.Result<ApiResponse>>()
    val result: LiveData<com.quantbit.accidentmanagement.ui.login.data.Result<ApiResponse>> get() = _result
    var user:String =""
    var token:String=""
    val registeredDevices = MutableLiveData<List<RegisteredDevice>>(listOf())



    init {
        val sharedUtility = SharedUtility(context = context)

       user = sharedUtility.getString("user").toString()
       token=  sharedUtility.getString("token").toString()

    }
    private val _userDataList = MutableLiveData<Result<ApiDataResponse>>()
    //
    //
    //
    // private val _userDataList = MutableLiveData<Result<ApiDataResponse>>()

    // Method to save contacts
//    fun saveContacts(userData: UserData) {
//        viewModelScope.launch {
//            try {
//                userData.user = user
//                // Call the suspend function and handle the result
//                val response = repository.saveContacts(userData)
//                _result.value = response
//            } catch (e: Exception) {
//                // Handle any exceptions that might occur
//                _result.value = com.quantbit.accidentmanagement.ui.login.data.Result.Error(IOException("Unexpected error saving contacts", e))
//            }
//        }
//    }

    fun saveContacts() {
        viewModelScope.launch {
            try {
                val contactData = contact.value ?: return@launch
                val devices = registeredDevices.value ?: listOf()

                val userData = UserData(user, listOf(contactData), devices)

                val response = repository.saveContacts(userData,token)
                _result.value = response
            } catch (e: Exception) {
                _result.value = com.quantbit.accidentmanagement.ui.login.data.Result.Error(IOException("Unexpected error saving contacts", e))
            }
        }
    }


    private val _userData = MutableLiveData<ApiDataResponse>()
    val userData: LiveData<ApiDataResponse> get() = _userData

//    fun getContacts(user: String) {
//        viewModelScope.launch {
//            try {
//                val result = repository.getContacts(user)
//
//            } catch (e: Exception) {
//              print(e.message)
//            }
//        }
//    }

    fun getContacts() {
        viewModelScope.launch {
            try {
                // Fetch the result from repository
                val result = repository.getContacts(user,token)

                // Handle the result
                when (result) {
                    is com.quantbit.accidentmanagement.ui.login.data.Result.Success -> {
                        // Set the value of _userData with the data from Result.Success
                        _userData.value = result.data
                        Log.d("EmergencyViewmodel",result.data.toString())
                    }
                    is com.quantbit.accidentmanagement.ui.login.data.Result.Error -> {
                        // Handle the error case
                       // _userData.value = ApiDataResponse("error","message",{]})  // Optionally set to null or handle it as needed
                        // Print or log the error message
                        print(result.exception.message)
                    }


                }
            } catch (e: Exception) {
                // Handle unexpected exceptions
                print(e.message)
            }
        }
    }





}
