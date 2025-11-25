package com.example.tccbebe.service

import com.example.tccbebe.model.CadastroRotina
import com.example.tccbebe.model.ViewRoutinesResponse
import com.example.tccbebe.model.ResponseCadastroRotina
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Headers
import retrofit2.http.POST

interface CadstroRotina {

    @Headers("Content-Type: application/json")
    @POST("RelacionamentoRotina/cadastro")
    fun cadastrarRotina(@Body cliente: CadastroRotina): retrofit2.Call<ResponseCadastroRotina>

    @Headers("Content-Type: application/json")
    @GET("viewRoutines")
    fun getAllRotinas(): retrofit2.Call<ViewRoutinesResponse>

    // Novo endpoint para retornar as rotinas de um usuário específico
    @Headers("Content-Type: application/json")
    @GET("viewUserRoutines/{id}")
    fun getUserRotinas(@Path("id") id: Int): retrofit2.Call<ViewRoutinesResponse>

}