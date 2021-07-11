package com.lucasginard.dolarpy

import android.app.Application
import androidx.room.Room
import com.lucasginard.dolarpy.database.DolarDatabase

class DolarApp : Application() {

    companion object {
        lateinit var database: DolarDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database =  Room.databaseBuilder(this, DolarDatabase::class.java, "dolar-db").build()
    }

}