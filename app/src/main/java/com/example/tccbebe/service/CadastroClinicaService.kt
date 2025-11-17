package com.example.tccbebe.service

import com.example.tccbebe.model.CadastroClinicaData
import com.example.tccbebe.model.ResponseCadastroClinica
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CadastroClinicaService {

    @Headers("Content-Type: application/json")
    @POST("clinica/cadastro")
    fun cadastrarUsuario(@Body cliente: CadastroClinicaData): Call<ResponseCadastroClinica>

}