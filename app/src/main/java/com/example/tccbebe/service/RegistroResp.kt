package com.example.tccbebe.service

import com.example.tccbebe.model.CadastroUser
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegistroResp {

    @Headers("Content-Type: application/json")
    @POST("resp/cadastro")
    fun cadastrarUsuario(@Body cliente: CadastroUser): retrofit2.Call<CadastroUser>

}