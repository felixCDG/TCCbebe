package com.example.tccbebe.service

import com.example.tccbebe.model.CadastroUser
import com.example.tccbebe.model.Login
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {

    @Headers("Content-Type: application/json")
    @POST("user/login")
    fun loginUsuario(@Body cliente: Login): retrofit2.Call<Login>

}