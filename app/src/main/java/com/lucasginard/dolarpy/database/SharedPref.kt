package com.lucasginard.dolarpy.database

import android.content.Context
import android.content.SharedPreferences

class SharedPref (context: Context) {
    val prefCotizacionScreen = "UpdateSave"
    val prefSplashDialogScreen = "saveSettings"

    val sharedNameUpdate = "lastUpdate"
    val sharedNameIsLess = "isLess"
    val sharedNameIsBuy = "isBuy"
    val sharedFlatMode = "flatSaveMode"
    val sharedFlatLenguaje = "flatSaveLenguaje"
    val sharedSaveMode = "saveMode"
    val sharedSaveLenguaje = "saveLenguaje"

    val prefsCotizacion: SharedPreferences = context.getSharedPreferences(prefCotizacionScreen, 0)
    val prefsSplashDialog: SharedPreferences = context.getSharedPreferences(prefSplashDialogScreen, 0)

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
        get() = prefsSplashDialog.getBoolean(sharedFlatMode, false)
        set(value) = prefsSplashDialog.edit().putBoolean(sharedFlatMode, value).apply()

    var saveMode: Int
        get() = prefsSplashDialog.getInt(sharedSaveMode, 3)
        set(value) = prefsSplashDialog.edit().putInt(sharedSaveMode, value).apply()

    var flatLenguaje: Boolean
        get() = prefsSplashDialog.getBoolean(sharedFlatLenguaje, false)
        set(value) = prefsSplashDialog.edit().putBoolean(sharedFlatLenguaje, value).apply()

    var saveLenguaje: String
        get() = prefsSplashDialog.getString(sharedSaveLenguaje, "").toString()
        set(value) = prefsSplashDialog.edit().putString(sharedSaveLenguaje, value).apply()

}