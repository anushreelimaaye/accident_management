package com.quantbit.accidentmanagement.ui.ble_events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.quantbit.accidentmanagement.databinding.ItemEventLogsBinding


class BLELogAdapter:ListAdapter<DailyLog,BLELogAdapter.LogViewHolder>(DeviceDiffCallback()) {


    class LogViewHolder(private val binding: ItemEventLogsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(log: DailyLog) {
            binding.log = log
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
       val binding = com.quantbit.accidentmanagement.databinding.ItemEventLogsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       return  LogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = getItem(position)
        holder.bind(log)
    }

    class DeviceDiffCallback : DiffUtil.ItemCallback<DailyLog>() {
        override fun areItemsTheSame(oldItem: DailyLog, newItem: DailyLog): Boolean {
           return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DailyLog, newItem: DailyLog): Boolean {
           return  oldItem == newItem
        }
    }
}


