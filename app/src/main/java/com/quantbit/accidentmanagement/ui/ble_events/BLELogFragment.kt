package com.quantbit.accidentmanagement.ui.ble_events

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.quantbit.accidentmanagement.R
import com.quantbit.accidentmanagement.databinding.FragmentBLELogBinding
import com.quantbit.accidentmanagement.databinding.FragmentEmergencyContactsBinding
import com.quantbit.accidentmanagement.ui.emergency_contacts.ContactRepository
import com.quantbit.accidentmanagement.ui.emergency_contacts.EmergencyContact
import com.quantbit.accidentmanagement.ui.emergency_contacts.EmergencyContactsViewModelFactory
import com.quantbit.accidentmanagement.ui.emergency_contacts.list_contact.EmergencyContactAdapter
import com.quantbit.accidentmanagement.ui.emergency_contacts.list_contact.EmergencyContactsViewModel
import com.quantbit.accidentmanagement.ui.home.DeviceAdapter
import java.util.Arrays

class BLELogFragment : Fragment() {

    private var _binding: FragmentBLELogBinding? = null
    private val binding get() = _binding!!

    private lateinit var bleLogAdapter: BLELogAdapter
    private lateinit var viewModel: BLELogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentBLELogBinding>(
            inflater, R.layout.fragment_b_l_e_log, container, false
        )

        val repository = LogRepository()
        viewModel = ViewModelProvider(this, LogEventsViewModelFactory( repository,requireContext(),))
            .get(BLELogViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val recyclerView = binding.logsList

        viewModel.connectionData.observe(getViewLifecycleOwner()) { contacts ->
            bleLogAdapter = BLELogAdapter()
            recyclerView.adapter = bleLogAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            bleLogAdapter.submitList(contacts.daily_logs)
//            contactAdapter.updateContacts(contacts.message.data.emergency_contacts)
//
//
//            binding.textViewNoContacts.visibility =
//                if (contacts.message.data.emergency_contacts.isEmpty()) View.VISIBLE else View.GONE
//
//            val deviceAdapter = DeviceAdapter()
//            binding.recyclerViewDevices.adapter = deviceAdapter
//            binding.recyclerViewDevices.layoutManager = LinearLayoutManager(requireContext())
//            deviceAdapter.submitList(contacts.message.data.registered_devices)
        }



        return binding.root
    }
}