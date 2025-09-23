package com.example.tccbebe.service

import com.example.tccbebe.model.CadastroUser
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CadastroService {

    @Headers("Content-Type: application/json")
    @POST("user")
    fun cadastrarUsuario(@Body cliente: CadastroUser): retrofit2.Call<CadastroUser>

}