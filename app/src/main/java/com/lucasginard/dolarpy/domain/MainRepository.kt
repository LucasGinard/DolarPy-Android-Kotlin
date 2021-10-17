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

    fun getUpdateText():String{
        return DolarApp.sharedPref.updateSave
    }

    fun saveUpdateText(update:String){
         DolarApp.sharedPref.updateSave = update
    }

    fun getIsBuySave():Boolean{
        return DolarApp.sharedPref.isBuy
    }

    fun saveIsBuy(isBuy:Boolean){
        DolarApp.sharedPref.isBuy = isBuy
    }

    fun getIsLessSave():Boolean{
        return DolarApp.sharedPref.isLess
    }

    fun saveIsLess(isLess:Boolean){
        DolarApp.sharedPref.isLess = isLess
    }

    fun getFlatMode():Boolean{
        return DolarApp.sharedPref.flatMode
    }

    fun saveFlatMode(mode:Boolean){
        DolarApp.sharedPref.flatMode = mode
    }

    fun getMode():Int{
        return DolarApp.sharedPref.saveMode
    }

    fun saveMode(mode:Int){
        DolarApp.sharedPref.saveMode = mode
    }

    fun FlatLenguaje():Boolean{
        return DolarApp.sharedPref.flatLenguaje
    }

    fun saveFlatLenguaje(flat:Boolean){
        DolarApp.sharedPref.flatLenguaje = flat
    }

    fun SaveLenguaje():String{
        return DolarApp.sharedPref.saveLenguaje
    }

    fun saveLenguaje(lenguaje:String){
        DolarApp.sharedPref.saveLenguaje = lenguaje
    }

}