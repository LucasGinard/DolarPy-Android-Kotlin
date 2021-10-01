package com.lucasginard.dolarpy.database

import androidx.room.*

@Dao
interface DolarDao {
    @Query("SELECT * FROM dolar_entity")
    fun getAllDolar(): MutableList<DolarEntity>

    @Insert
    fun addDolar(dolarEntity : DolarEntity):Long

    @Query("SELECT * FROM dolar_entity where id = (:id)")
    fun getDolarById(id: Long): DolarEntity

    @Update
    suspend fun updateDolar(dolarEntity: DolarEntity):Int

}