package com.lucasginard.dolarpy

import android.app.Application
import androidx.room.Room
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.lucasginard.dolarpy.database.DolarDatabase
import com.lucasginard.dolarpy.database.SharedPref

class DolarApp : Application() {

    companion object {
        lateinit var database: DolarDatabase
        lateinit var sharedPref: SharedPref
    }

    override fun onCreate() {
        super.onCreate()
        database =  Room.databaseBuilder(this, DolarDatabase::class.java, "dolar-db").build()
        sharedPref = SharedPref(applicationContext)
        if(BuildConfig.DEBUG){
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
        }
    }

}