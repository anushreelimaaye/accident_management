package com.quantbit.accidentmanagement.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.quantbit.accidentmanagement.databinding.ItemDeviceBinding
import com.quantbit.accidentmanagement.databinding.ItemRegisteredDeviceBinding
import com.quantbit.accidentmanagement.ui.emergency_contacts.RegisteredDevice

class DeviceAdapter : ListAdapter<RegisteredDevice, DeviceAdapter.DeviceViewHolder>(DeviceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = ItemRegisteredDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = getItem(position)
        holder.bind(device)
    }

    class DeviceViewHolder(private val binding: ItemRegisteredDeviceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(device: RegisteredDevice) {
            binding.device = device
            binding.executePendingBindings()
        }
    }

    class DeviceDiffCallback : DiffUtil.ItemCallback<RegisteredDevice>() {
        override fun areItemsTheSame(oldItem: RegisteredDevice, newItem: RegisteredDevice): Boolean {
            return oldItem.device_name == newItem.device_name // Use a unique identifier if available
        }

        override fun areContentsTheSame(oldItem: RegisteredDevice, newItem: RegisteredDevice): Boolean {
            return oldItem == newItem
        }
    }
}
