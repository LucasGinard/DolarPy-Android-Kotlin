package com.lucasginard.dolarpy.view.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucasginard.dolarpy.DolarApp
import com.lucasginard.dolarpy.database.DolarEntity
import com.lucasginard.dolarpy.dolarpyResponse
import com.lucasginard.dolarpy.domain.MainRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel constructor(private val repository: MainRepository)  : ViewModel() {

    val getDolarList = MutableLiveData<dolarpyResponse>()
    val errorMessage = MutableLiveData<String>()

    fun getAllDolar() {
        val response = repository.getAllDolar()
        response.enqueue(object : Callback<dolarpyResponse> {
            override fun onResponse(call: Call<dolarpyResponse>, response: Response<dolarpyResponse>) {
                getDolarList.postValue(response.body())
            }

            override fun onFailure(call: Call<dolarpyResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getAllDolarListSave():MutableList<DolarEntity> {
        return repository.getSaveDolar()
    }

    fun addDolar(dolar: DolarEntity,list:MutableList<DolarEntity>){
        repository.addSave(dolar,list)
    }

    fun updateDolar(dolar: DolarEntity){
        GlobalScope.launch {
            repository.updateDolar(dolar)
        }
    }

    fun getLastUpdateText():String{
        return repository.getUpdateText()
    }

    fun setLastUpdateText(update:String){
        repository.saveUpdateText(update)
    }

    fun getIsBuy():Boolean{
        return repository.getIsBuySave()
    }

    fun setIsBuy(isBuy:Boolean){
        repository.saveIsBuy(isBuy)
    }

    fun getIsLess():Boolean{
        return repository.getIsLessSave()
    }

    fun setIsLess(isLess:Boolean){
        repository.saveIsLess(isLess)
    }

    fun getFlatModeTheme():Boolean{
        return repository.getFlatMode()
    }

    fun setFlatMode(mode:Boolean){
        repository.saveFlatMode(mode)
    }

    fun getMode():Int{
        return repository.getMode()
    }

    fun saveMode(mode:Int){
        repository.saveMode(mode)
    }

    fun getFlatLenguaje():Boolean{
        return repository.FlatLenguaje()
    }

    fun setFlatLenguaje(flat:Boolean){
        repository.saveFlatLenguaje(flat)
    }

    fun getSaveLenguaje():String{
        return repository.SaveLenguaje()
    }

    fun setSaveLenguaje(lenguaje:String){
        repository.saveLenguaje(lenguaje)
    }
}