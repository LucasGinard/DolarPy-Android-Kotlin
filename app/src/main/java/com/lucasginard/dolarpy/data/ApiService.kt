package com.lucasginard.dolarpy.data

import com.lucasginard.dolarpy.data.model.dolarpyResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface apiService {

    @GET("1.0")
    fun getDolar(): Call<dolarpyResponse>

    companion object {

        var retrofitService: apiService? = null

        fun getInstance() : apiService {

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://dolar.melizeche.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(apiService::class.java)
            }
            return retrofitService!!
        }
    }

}