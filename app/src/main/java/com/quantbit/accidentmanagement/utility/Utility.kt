package com.quantbit.accidentmanagement.utility

import android.content.Context
import android.os.BatteryManager

class Utility {

    companion object{

        fun getBatteryPercentage(context: Context): Float {
            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY).toFloat()
        }
    }
}