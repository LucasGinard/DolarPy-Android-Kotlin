package com.lucasginard.dolarpy.data

import com.lucasginard.dolarpy.dolarpyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface apiService {
    @GET
    suspend fun getDolar(@Url url:String): Response<dolarpyResponse>
}