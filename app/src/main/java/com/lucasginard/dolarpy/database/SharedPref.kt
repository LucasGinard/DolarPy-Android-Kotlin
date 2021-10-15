package com.lucasginard.dolarpy.database

import android.content.Context
import android.content.SharedPreferences

class SharedPref (context: Context) {
    val prefCotizacionScreen = "UpdateSave"
    val sharedNameUpdate = "lastUpdate"
    val sharedNameIsLess = "isLess"
    val sharedNameIsBuy = "isBuy"

    val prefs: SharedPreferences = context.getSharedPreferences(prefCotizacionScreen, 0)

    var updateSave: String
        get() = prefs.getString(sharedNameUpdate, "").toString()
        set(value) = prefs.edit().putString(sharedNameUpdate, value).apply()

    var isLess: Boolean
        get() = prefs.getBoolean(sharedNameIsLess, true)
        set(value) = prefs.edit().putBoolean(sharedNameIsLess, value).apply()

    var isBuy: Boolean
        get() = prefs.getBoolean(sharedNameIsBuy, true)
        set(value) = prefs.edit().putBoolean(sharedNameIsBuy, value).apply()
}