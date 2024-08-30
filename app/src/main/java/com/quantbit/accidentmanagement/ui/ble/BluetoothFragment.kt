package com.quantbit.accidentmanagement.ui.ble


import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quantbit.accidentmanagement.databinding.FragmentBluetoothBinding

import com.quantbit.accidentmanagement.service.BleService
import com.quantbit.accidentmanagement.ui.BaseFragment
import com.quantbit.accidentmanagement.ui.home.BluetoothDeviceAdapter
import com.quantbit.accidentmanagement.ui.home.OnItemClickListener


class BluetoothFragment : BaseFragment(), OnItemClickListener {

    private var _binding: FragmentBluetoothBinding? = null
    private val binding get() = _binding!!
    private val bluetoothViewModel: BluetoothViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BluetoothDeviceAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      //  val slideshowViewModel = ViewModelProvider(this).get(BluetoothViewModel::class.java)

        _binding = com.quantbit.accidentmanagement.databinding.FragmentBluetoothBinding.inflate(inflater, container, false)
        val root: View = binding.root
        init()

        requestPermissions(
            arrayOf(
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.ACCESS_FINE_LOCATION
                )
        )

        return root
    }

    fun init(){
        recyclerView = _binding!!.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = BluetoothDeviceAdapter(requireContext(), this)

        recyclerView.adapter = adapter

        bluetoothViewModel.discoveredDevices.observe(viewLifecycleOwner) { devices ->
            // Update UI with the list of discovered devices
            adapter.submitList(devices)
        }

        bluetoothViewModel.connectionStatus.observe(viewLifecycleOwner) { statusMap ->
            // Update adapter with connection status
            adapter.updateConnectionStatus(statusMap)
            adapter.notifyDataSetChanged()  // This can be optimized further based on your use case
        }

        bluetoothViewModel.connectionStatus.observe(viewLifecycleOwner, Observer { statusMap ->
            adapter.updateConnectionStatus(statusMap)
        })

        binding.reloadButton.setOnClickListener(View.OnClickListener {
            init()
        //
        //          bluetoothViewModel.startDiscovery()
//            adapter.submitList(bluetoothViewModel.discoveredDevices.value)
        })


//        if (adapter.currentList.isEmpty()) {
//            _binding!!.noDevicesText.visibility = View.VISIBLE
//            recyclerView.visibility = View.GONE
//        } else {
//            _binding!!.noDevicesText.visibility = View.VISIBLE
//            recyclerView.visibility = View.VISIBLE
//        }


    }

    override fun onPermissionsResult(allGranted: Boolean) {
        if (allGranted) {
            // Permissions granted, proceed with your logic
            //startBluetoothDiscovery()
            bluetoothViewModel.startDiscovery()
        } else {
            Toast.makeText(requireContext(), "All permissions are required.", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(device: BluetoothDevice) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val intent = Intent(context, BleService::class.java)
        intent.putExtra("device_address", device.address)
        intent.putExtra("device_name", device.name)
        context?.startService(intent)

      //  bluetoothViewModel.connectToDevice(device)
    }


}