package com.lucasginard.dolarpy.database

import android.content.Context
import android.content.SharedPreferences

class SharedPref (context: Context) {
    val prefCotizacionScreen = "UpdateSave"
    val prefSplashScreen = "saveSettings"

    val sharedNameUpdate = "lastUpdate"
    val sharedNameIsLess = "isLess"
    val sharedNameIsBuy = "isBuy"
    val sharedFlatMode = "flatSaveMode"
    val sharedSaveMode = "saveMode"

    val prefsCotizacion: SharedPreferences = context.getSharedPreferences(prefCotizacionScreen, 0)
    val prefsSplash: SharedPreferences = context.getSharedPreferences(prefSplashScreen, 0)

    var updateSave: String
        get() = prefsCotizacion.getString(sharedNameUpdate, "").toString()
        set(value) = prefsCotizacion.edit().putString(sharedNameUpdate, value).apply()

    var isLess: Boolean
        get() = prefsCotizacion.getBoolean(sharedNameIsLess, true)
        set(value) = prefsCotizacion.edit().putBoolean(sharedNameIsLess, value).apply()

    var isBuy: Boolean
        get() = prefsCotizacion.getBoolean(sharedNameIsBuy, true)
        set(value) = prefsCotizacion.edit().putBoolean(sharedNameIsBuy, value).apply()

    var flatMode: Boolean
        get() = prefsSplash.getBoolean(sharedFlatMode, false)
        set(value) = prefsCotizacion.edit().putBoolean(sharedFlatMode, value).apply()

    var saveMode: Int
        get() = prefsSplash.getInt(sharedSaveMode, 3)
        set(value) = prefsCotizacion.edit().putInt(sharedSaveMode, value).apply()
}