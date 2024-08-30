package com.quantbit.accidentmanagement.ui.home

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.quantbit.accidentmanagement.databinding.ItemDeviceBinding
import com.quantbit.accidentmanagement.utility.SharedUtility

interface OnItemClickListener {
    fun onItemClick(device: BluetoothDevice)
}

class BluetoothDeviceAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : ListAdapter<BluetoothDevice, BluetoothDeviceAdapter.BluetoothDeviceViewHolder>(DeviceDiffCallback()) {


    private val deviceConnectionStatus = mutableMapOf<BluetoothDevice, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothDeviceViewHolder {
        val binding = ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BluetoothDeviceViewHolder(binding, listener, context)
    }

    override fun onBindViewHolder(holder: BluetoothDeviceViewHolder, position: Int) {
        val device = getItem(position)
        holder.bind(device, deviceConnectionStatus[device] ?: false)
    }

    fun updateConnectionStatus(statusMap: Map<BluetoothDevice, Boolean>) {
        deviceConnectionStatus.clear()
        deviceConnectionStatus.putAll(statusMap)
        notifyDataSetChanged()  // Notify the adapter that the data has changed
    }

    class BluetoothDeviceViewHolder(
        private val binding: ItemDeviceBinding,
        private val listener: OnItemClickListener,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(device: BluetoothDevice, isConnected: Boolean) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            val sharedUtility = SharedUtility(context)
            val status =sharedUtility.getString("status")
            binding.deviceName.text = device.name
            binding.deviceAddress.text = device.address

            binding.deviceStatus.text = status
            binding.deviceStatus.setTextColor(
                if (device.name == "Connected") ContextCompat.getColor(context, android.R.color.holo_green_dark)
                else ContextCompat.getColor(context, android.R.color.holo_red_dark)
            )

            // Set the onClickListener for the item
            itemView.setOnClickListener {
                listener.onItemClick(device)
            }
        }
    }

    class DeviceDiffCallback : DiffUtil.ItemCallback<BluetoothDevice>() {

        override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
            return oldItem == newItem
        }
    }
}
