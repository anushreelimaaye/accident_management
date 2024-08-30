package com.quantbit.accidentmanagement.ui.emergency_contacts.list_contact

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.quantbit.accidentmanagement.R
import com.quantbit.accidentmanagement.databinding.ItemEmergencyContactsBinding

import com.quantbit.accidentmanagement.ui.emergency_contacts.EmergencyContact


interface OnContactClickListener {
    fun onItemClick(emergencyContact: EmergencyContact)
}

class EmergencyContactAdapter(
    private var contacts: List<EmergencyContact>,private val listener: OnContactClickListener
) : ListAdapter<EmergencyContact, EmergencyContactAdapter.ContactViewHolder>(EmergencyContactAdapter.ContactDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = DataBindingUtil.inflate<com.quantbit.accidentmanagement.databinding.ItemEmergencyContactsBinding>(
            LayoutInflater.from(parent.context), R.layout.item_emergency_contacts, parent, false
        )
        return ContactViewHolder(binding,listener)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        if (position < contacts.size) {  // Check index validity
            val contact: EmergencyContact
            = contacts[position]
            holder.bind(contact)
        }

    }
    fun updateContacts(contacts: List<EmergencyContact>?) {
        this.contacts = contacts!!
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
      return contacts.size
    }


    class ContactViewHolder(private val binding: ItemEmergencyContactsBinding,private val listener: OnContactClickListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: EmergencyContact) {
            binding.contact = contact
            binding.executePendingBindings()
            // Set the onClickListener for the item
            itemView.setOnClickListener {
                listener.onItemClick(contact)
            }
        }

    }

    class ContactDiffCallback : DiffUtil.ItemCallback<EmergencyContact>() {
        override fun areItemsTheSame(oldItem: EmergencyContact, newItem: EmergencyContact): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: EmergencyContact, newItem: EmergencyContact): Boolean {
            return oldItem == newItem
        }
    }
}
