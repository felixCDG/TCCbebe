package com.example.tccbebe.service

import android.telecom.Call
import com.example.tccbebe.model.RegistroResp
import com.example.tccbebe.model.ResponsePerfilResp
import com.example.tccbebe.model.ResponseRegistroResp
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface RegistroResp {

    @Headers("Content-Type: application/json")
    @POST("resp/cadastro")
    fun cadastrarResponsavel(@Body cliente: RegistroResp): retrofit2.Call<ResponseRegistroResp>

    @Headers("Content-Type: application/json")
    @GET("resp/{id}")
    fun getResponsavelById(@Path("id") id: Int): retrofit2.Call<ResponsePerfilResp>

}