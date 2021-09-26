package com.lucasginard.dolarpy.domain

import com.lucasginard.dolarpy.DolarApp
import com.lucasginard.dolarpy.data.apiService
import com.lucasginard.dolarpy.database.DolarEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainRepository constructor(private val retrofitService: apiService) {
    fun getAllDolar() = retrofitService.getDolar()

    fun getSaveDolar() = DolarApp.database.dolarDao().getAllDolar()

    fun addSave(dolar: DolarEntity, list:MutableList<DolarEntity>) = GlobalScope.launch {
        val id = DolarApp.database.dolarDao().addDolar(dolar)
        val recoveryDolar = DolarApp.database.dolarDao().getDolarById(id)
        list.add(recoveryDolar)
    }

    fun updateDolar(dolar: DolarEntity) = GlobalScope.launch {
        DolarApp.database.dolarDao().updateDolar(dolar)
    }

}