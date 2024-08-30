package com.quantbit.accidentmanagement.ui.notification_log

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.quantbit.accidentmanagement.databinding.ItemNotificationLogBinding


class NotificationLogAdapter: ListAdapter<Contact, NotificationLogAdapter.NotificationViewHolder>(NotificationLogAdapter.DeviceDiffCallback())  {

    class NotificationViewHolder(private val binding:ItemNotificationLogBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(contact:Contact){
            binding.contact = contact
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = com.quantbit.accidentmanagement.databinding.ItemNotificationLogBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return NotificationLogAdapter.NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val log = getItem(position)
        holder.bind(log)
    }


    class DeviceDiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return  oldItem == newItem
        }
    }

}

