package com.example.tccbebe.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Conexao {

    private val BASE_URL = "https://backend-sosbaby.onrender.com/v1/sosbaby/"

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

    fun getItemRotinaService(): CadstroItemRotina{
        return conexao.create(CadstroItemRotina::class.java)
    }

    fun getRotinaService(): CadstroRotina{
        return conexao.create(CadstroRotina::class.java)
    }

    fun getCadastroClinicaService(): CadastroClinicaService{
        return conexao.create(CadastroClinicaService::class.java)
    }

    // Expose RoomService to allow checking whether a room exists before navigating
    fun getRoomService(): RoomService {
        return conexao.create(RoomService::class.java)
    }

}