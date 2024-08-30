package com.quantbit.accidentmanagement.ui.emergency_contacts

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quantbit.accidentmanagement.ui.emergency_contacts.list_contact.EmergencyContactsViewModel

class EmergencyContactsViewModelFactory(
    private val repository: ContactRepository,
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmergencyContactsViewModel::class.java)) {
            return EmergencyContactsViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}