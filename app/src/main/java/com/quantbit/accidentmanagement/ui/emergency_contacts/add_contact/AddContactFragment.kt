package com.quantbit.accidentmanagement.ui.emergency_contacts.add_contact

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.quantbit.accidentmanagement.R
import com.quantbit.accidentmanagement.databinding.FragmentAddContactBinding

import com.quantbit.accidentmanagement.ui.emergency_contacts.ContactRepository
import com.quantbit.accidentmanagement.ui.emergency_contacts.EmergencyContactsViewModelFactory
import com.quantbit.accidentmanagement.ui.emergency_contacts.list_contact.EmergencyContactsViewModel

class AddContactFragment : Fragment() {

    companion object {
        fun newInstance() = AddContactFragment()
    }

    private lateinit var viewModel: EmergencyContactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentAddContactBinding>(
            inflater, R.layout.fragment_add_contact, container, false
        )
        val repository = ContactRepository()
        viewModel = ViewModelProvider(this, EmergencyContactsViewModelFactory( repository,requireContext(),))
            .get(EmergencyContactsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }
}