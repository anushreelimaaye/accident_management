package com.quantbit.accidentmanagement.ui.ble_events

data class AddBleLogRequest (
                              val user:String,
                              val device_name:String,
                              val device_address:String,
                              val last_connection_time: String,
                              val connection_status: String,
                              val battery_status: String,
                              val daily_logs: List<DailyLog>)