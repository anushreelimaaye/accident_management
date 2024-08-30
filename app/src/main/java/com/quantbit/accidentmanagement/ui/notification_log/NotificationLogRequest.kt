package com.quantbit.accidentmanagement.ui.notification_log

import com.quantbit.accidentmanagement.ui.ble_events.Message

class NotificationLogRequest(
    val user: String,
    val contact_data_json: List<Contact>
)

data class Contact(
    val contact_name: String,
    val phone: String,
    val sms_message: String
)

data class NotificationLogDetails(
    val status: String,
    val message: String,
    val data: List<Contact>
)

data class NotificationLogResponse (val message: NotificationLogDetails)
