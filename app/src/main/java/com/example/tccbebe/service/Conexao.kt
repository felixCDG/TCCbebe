package com.example.tccbebe.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Conexao {

    private val BASE_URL = "http://10.0.2.2:3030/v1/"

    private val conexao= Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getCadastroService(): CadastroService{
        return conexao.create(CadastroService::class.java)
    }

}