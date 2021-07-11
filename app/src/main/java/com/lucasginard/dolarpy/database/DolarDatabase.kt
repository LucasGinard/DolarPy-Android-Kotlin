package com.lucasginard.dolarpy.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(DolarEntity::class), version = 1)
abstract class DolarDatabase  : RoomDatabase() {
    abstract fun dolarDao(): DolarDao
}