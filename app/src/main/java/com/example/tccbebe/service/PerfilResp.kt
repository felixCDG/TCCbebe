package com.example.tccbebe.service

import com.example.tccbebe.model.RegistroBebe
import com.example.tccbebe.model.RegistroResp
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface PerfilResp {

    @Headers("Content-Type: application/json")
    @GET("resp/{id}")
    suspend fun perfildoResp(@Path("id") id: Int): RegistroResp

}