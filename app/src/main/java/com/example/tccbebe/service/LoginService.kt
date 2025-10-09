package com.example.tccbebe.service

import com.example.tccbebe.model.CadastroUser
import com.example.tccbebe.model.Login
import com.example.tccbebe.model.ResponseLoginUser
import com.example.tccbebe.model.ResponsePerfilResp
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface LoginService {

    @Headers("Content-Type: application/json")
    @POST("user/login")
    fun loginUsuario(@Body cliente: Login): retrofit2.Call<ResponseLoginUser>


}