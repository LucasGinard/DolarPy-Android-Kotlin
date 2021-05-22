package com.lucasginard.dolarpy.domain

import com.lucasginard.dolarpy.data.apiService

class MainRepository constructor(private val retrofitService: apiService) {
    fun getAllDolar() = retrofitService.getDolar()
}