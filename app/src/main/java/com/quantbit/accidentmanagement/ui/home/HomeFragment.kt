package com.quantbit.accidentmanagement.ui.home

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quantbit.accidentmanagement.databinding.FragmentHomeBinding
import com.quantbit.accidentmanagement.service.BleService


class HomeFragment : Fragment(), OnItemClickListener {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BluetoothDeviceAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        init()
        return root
    }
    fun init(){
        recyclerView = _binding!!.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = BluetoothDeviceAdapter(requireContext(), this)
        recyclerView.adapter = adapter


        viewModel.pairedDevices.observe(viewLifecycleOwner) { devices ->
            // Update UI with the list of discovered devices
            adapter.submitList(devices)
            if (devices.size > 0) {
                _binding!!.noDevicesCardView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            } else {
                _binding!!.noDevicesCardView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }

        }
        binding.reloadButton.setOnClickListener(View.OnClickListener {
            viewModel.loadPairedDevices()
           // init()
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPairedDevices()
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
            Toast.makeText(context,"Please make sure all permissions are granted",Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(context,"Please wait for a while",Toast.LENGTH_SHORT).show()
        if (viewModel.isDeviceAvailableForConnection(device)) {
            val intent = Intent(context, BleService::class.java)
            intent.putExtra("device_address", device.address)

            intent.putExtra("device_name", device.name)
            context?.startService(intent)
        }else{

            Toast.makeText(context,"Device is not available for connection",Toast.LENGTH_SHORT).show()

        }
    }
}