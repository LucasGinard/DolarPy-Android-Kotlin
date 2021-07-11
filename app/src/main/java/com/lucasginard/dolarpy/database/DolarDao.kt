package com.lucasginard.dolarpy.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DolarDao {
    @Query("SELECT * FROM dolar_entity")
    fun getAllDolar(): MutableList<DolarEntity>

    @Insert
    fun addDolar(dolarEntity : DolarEntity):Long

    @Query("SELECT * FROM dolar_entity where id = (:id)")
    fun getDolarById(id: Long): DolarEntity

    @Delete
    fun deleteTask(dolarEntity: DolarEntity):Int

    @Query("DELETE FROM dolar_entity")
    fun deleteDates()
}