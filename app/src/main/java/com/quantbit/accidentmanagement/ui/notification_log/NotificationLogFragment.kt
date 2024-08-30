package com.quantbit.accidentmanagement.ui.notification_log

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
import com.quantbit.accidentmanagement.databinding.FragmentNotificationLogBinding
import com.quantbit.accidentmanagement.ui.ble_events.BLELogAdapter
import com.quantbit.accidentmanagement.ui.ble_events.BLELogViewModel
import com.quantbit.accidentmanagement.ui.ble_events.LogEventsViewModelFactory
import com.quantbit.accidentmanagement.ui.ble_events.LogRepository

class NotificationLogFragment : Fragment() {
    private lateinit var notificationLogAdapter: NotificationLogAdapter
    private lateinit var viewModel: NotificationLogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DataBindingUtil.inflate<FragmentNotificationLogBinding>(
            inflater, R.layout.fragment_notification_log, container, false
        )

        val repository = LogRepository()
        viewModel = ViewModelProvider(this, NotificationViewModelFactory( repository,requireContext(),))
            .get(NotificationLogViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val recyclerView = binding.contactList

        viewModel.contactList.observe(getViewLifecycleOwner()) { contacts ->
            notificationLogAdapter = NotificationLogAdapter()
            recyclerView.adapter = notificationLogAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            notificationLogAdapter.submitList(contacts)
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