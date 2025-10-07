package com.example.tccbebe.service

import com.example.tccbebe.model.RegistroResp
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegistroResp {

    @Headers("Content-Type: application/json")
    @POST("resp/cadastro")
    fun cadastrarResponsavel(@Body cliente: RegistroResp): retrofit2.Call<RegistroResp>

}