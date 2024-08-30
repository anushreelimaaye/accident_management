package com.quantbit.accidentmanagement.ui.emergency_contacts.add_contact

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quantbit.accidentmanagement.ui.emergency_contacts.EmergencyContact

class AddContactViewModel : ViewModel() {
    val contact = MutableLiveData(EmergencyContact("", "", "", ""))

    fun saveContact() {
        val contact = contact.value ?: return
        // Save contact to your data source (e.g., database or shared preferences)
        // Notify parent fragment or activity about the new contact
    }
}