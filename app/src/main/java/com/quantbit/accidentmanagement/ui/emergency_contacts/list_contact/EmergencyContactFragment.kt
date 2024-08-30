package com.quantbit.accidentmanagement.ui.emergency_contacts.list_contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.quantbit.accidentmanagement.R
import com.quantbit.accidentmanagement.databinding.FragmentEmergencyContactsBinding
import com.quantbit.accidentmanagement.ui.emergency_contacts.ContactRepository
import com.quantbit.accidentmanagement.ui.emergency_contacts.EmergencyContact
import com.quantbit.accidentmanagement.ui.emergency_contacts.EmergencyContactsViewModelFactory
import com.quantbit.accidentmanagement.ui.home.DeviceAdapter
import java.util.Arrays


class EmergencyContactFragment : Fragment(),OnContactClickListener {

    private var _binding: FragmentEmergencyContactsBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactAdapter: EmergencyContactAdapter
    private val contacts = mutableListOf<EmergencyContact>()
    private lateinit var viewModel: EmergencyContactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentEmergencyContactsBinding>(
            inflater, R.layout.fragment_emergency_contacts, container, false
        )

        val repository = ContactRepository()
        viewModel = ViewModelProvider(this, EmergencyContactsViewModelFactory( repository,requireContext(),))
            .get(EmergencyContactsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // Create a dummy list of contacts
        // Create a dummy list of contacts
        val dummyContacts: List<EmergencyContact> = Arrays.asList<EmergencyContact>(
            EmergencyContact("John Doe", "1234567890", "john@example.com", "Friend"),
            EmergencyContact("Jane Smith", "0987654321", "jane@example.com", "Colleague")
        )


        val recyclerView = binding.recyclerViewContacts
        viewModel.getContacts()


//        viewModel.contactList.observe(viewLifecycleOwner, Observer {
//            contactAdapter.submitList(it)
//        })


        binding.buttonAddContact.setOnClickListener {
            addContact()
        }

        viewModel.userData.observe(viewLifecycleOwner, { result ->

        })

        viewModel.userData.observe(getViewLifecycleOwner()) { contacts ->
            contactAdapter = EmergencyContactAdapter(contacts.message.data.emergency_contacts,this)
            recyclerView.adapter = contactAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            contactAdapter.updateContacts(contacts.message.data.emergency_contacts)


            binding.textViewNoContacts.visibility =
                if (contacts.message.data.emergency_contacts.isEmpty()) View.VISIBLE else View.GONE

           val deviceAdapter = DeviceAdapter()
            binding.recyclerViewDevices.adapter = deviceAdapter
            binding.recyclerViewDevices.layoutManager = LinearLayoutManager(requireContext())
            deviceAdapter.submitList(contacts.message.data.registered_devices)
        }

//        binding.textViewNoContacts.visibility =
//                if (contacts.isEmpty()) View.VISIBLE else View.GONE
//        binding.recyclerViewContacts.visibility =
//            if (contacts.isEmpty()) View.GONE else View.VISIBLE

        return binding.root
    }



    fun addContact() {
        // Navigate to AddContactFragment
        // Assuming you are using a navigation component
        findNavController().navigate(R.id.action_nav_contact_list_to_nav_add_contact)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

   override fun onItemClick(emergencyContact: EmergencyContact) {
//        val action = EmergencyContactFragmentDirections.actionNavContactListToNavAddContact(emergencyContact)
//        findNavController().navigate(action)
 }
}