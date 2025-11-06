package com.example.tccbebe.service

import com.example.tccbebe.model.RegistroBebe
import com.example.tccbebe.model.RegistroResp
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegistroBebe {

    @Headers("Content-Type: application/json")
    @POST("baby/cadastro")
    fun cadastroBabe(@Body cliente: RegistroBebe): retrofit2.Call<RegistroBebe>

}