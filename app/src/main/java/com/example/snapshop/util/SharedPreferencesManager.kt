package com.example.snapshop.util

import android.content.Context
import android.content.SharedPreferences

//class SharedPreferencesManager(context: Context) {
object SharedPreferencesManager {

    private const val PREF_NAME = "wishlist_prefs"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

}