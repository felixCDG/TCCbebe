package com.example.tccbebe.service

import android.content.Context
import com.example.senai.sp.testecalendario.service.RetrofitClient

class AuthenticatedConexao(private val context: Context) {

    private val authenticatedRetrofit = RetrofitClient.getInstance(context)

    fun getRegistroBebeService(): RegistroBebe {
        return authenticatedRetrofit.create(RegistroBebe::class.java)
    }

    fun getCadastroService(): CadastroService {
        return authenticatedRetrofit.create(CadastroService::class.java)
    }

    fun getLoginService(): LoginService {
        return authenticatedRetrofit.create(LoginService::class.java)
    }

    fun getRegistroRspService(): RegistroResp {
        return authenticatedRetrofit.create(RegistroResp::class.java)
    }


    fun getRotinaService(): CadstroRotina {
        return authenticatedRetrofit.create(CadstroRotina::class.java)
    }

    fun getChatIAService(): ChatIAApiService {
        return authenticatedRetrofit.create(ChatIAApiService::class.java)
    }

    fun getVideoChamadaService(): VideoChamadaApiService {
        return authenticatedRetrofit.create(VideoChamadaApiService::class.java)
    }

    fun getCadstroClinica(): CadastroClinicaService {
        return authenticatedRetrofit.create(CadastroClinicaService::class.java)
    }
}
