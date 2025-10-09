package com.example.tccbebe.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Conexao {

    private val BASE_URL = "http://10.0.2.2:3030/v1/sosbaby/"

    private val conexao= Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getCadastroService(): CadastroService{
        return conexao.create(CadastroService::class.java)
    }

    fun getLoginService(): LoginService{
        return conexao.create(LoginService::class.java)
    }

    fun getRegistroRspService(): RegistroResp{
        return conexao.create(RegistroResp::class.java)
    }

    fun getRegistroBebeService(): RegistroBebe{
        return conexao.create(RegistroBebe::class.java)
    }

    fun getPerfilRespService(): PerfilResp{
        return conexao.create(PerfilResp::class.java)
    }


}