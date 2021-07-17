package com.lucasginard.dolarpy.view.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucasginard.dolarpy.dolarpyResponse
import com.lucasginard.dolarpy.domain.MainRepository
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
}