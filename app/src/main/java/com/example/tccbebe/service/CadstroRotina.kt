package com.example.tccbebe.service

import com.example.tccbebe.model.CadastroItemRotina
import com.example.tccbebe.model.CadastroRotina
import com.example.tccbebe.model.ResponseCadastroItemRotina
import com.example.tccbebe.model.ResponseCadastroRotina
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CadstroRotina {

    @Headers("Content-Type: application/json")
    @POST("routineResp/cadastro")
    fun cadastrarRotina(@Body cliente: CadastroRotina): retrofit2.Call<ResponseCadastroRotina>

}