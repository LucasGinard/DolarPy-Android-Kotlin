package com.lucasginard.dolarpy.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dolar_entity")
data class DolarEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    var name:String = "",
    var buy:Double = 0.0,
    var sell:Double = 0.0
)