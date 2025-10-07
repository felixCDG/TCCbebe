package com.example.tccbebe.service

import com.example.tccbebe.model.CadastroUser
import com.example.tccbebe.model.ResponseCadastroUser
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CadastroService {

    @Headers("Content-Type: application/json")
    @POST("user/cadastro")
    fun cadastrarUsuario(@Body cliente: CadastroUser): retrofit2.Call<ResponseCadastroUser>

}