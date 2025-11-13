package com.example.tccbebe.service

import com.example.tccbebe.model.CadastroItemRotina
import com.example.tccbebe.model.CadastroUser
import com.example.tccbebe.model.ResponseCadastroItemRotina
import com.example.tccbebe.model.ResponseCadastroUser
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CadstroItemRotina {

    @Headers("Content-Type: application/json")
    @POST("routineItem/cadastro")
    fun cadastrarItemR(@Body cliente: CadastroItemRotina): retrofit2.Call<ResponseCadastroItemRotina>
}