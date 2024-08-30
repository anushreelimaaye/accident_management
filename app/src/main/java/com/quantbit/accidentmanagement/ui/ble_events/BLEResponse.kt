package com.quantbit.accidentmanagement.ui.ble_events

data class BLEResponse (val message: Message)

data class Message(
    val status: String,
    val message: MessageDetails
)

data class MessageDetails(
    val last_connection_time: String,
    val connection_status: String,
    val battery_status: String,
    val daily_logs: List<DailyLog>
)

data class DailyLog(
    val log_id: Any?,  // Use Any? to allow for null values; replace with the actual type if known
    val date: String,
    val status: String,
    val time: String,
    val device_battery_status: String,
    val event: String
)

data class BLELogRequest(
    val device_name: String,
    val device_address: String
)