package com.quantbit.accidentmanagement.utility

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quantbit.accidentmanagement.ui.emergency_contacts.EmergencyContact
import java.lang.reflect.Type;


class SharedUtility(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "Shared_Prefs"
    }

    // Common method to save a string in SharedPreferences
    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    // Methods to retrieve data from SharedPreferences
    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    // Methods to retrieve data from SharedPreferences
    fun getBoolean(key: String): Boolean? {
        return sharedPreferences.getBoolean(key, false)
    }

    // Common method to save a string in SharedPreferences
    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }


    fun saveEmergencyContacts(context: Context, contacts: List<EmergencyContact?>?) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(contacts)
        editor.putString("contacts", json)
        editor.apply() // or commit() for synchronous saving
    }

    fun getEmergencyContacts(context: Context): List<EmergencyContact> {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("contacts", null)
        val gson = Gson()
        val type: Type = object : TypeToken<List<EmergencyContact?>?>() {}.type
        return gson.fromJson(json, type)
    }
}
