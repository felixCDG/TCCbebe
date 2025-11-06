package com.example.senai.sp.testecalendario.service

import android.content.Context
import android.util.Log
import com.example.tccbebe.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Se estiver usando dispositivo físico, troque para o IP da sua máquina (ex: "http://192.168.x.x:3030/")
    private const val BASE_URL = "http://10.0.2.2:3030/v1/sosbaby/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Interceptor para adicionar o token de autorização
    private fun createAuthInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val token = SessionManager.getBearerToken(context)
            
            // 🔍 LOG DETALHADO - Mostra o que está sendo enviado
            Log.d("AUTH_INTERCEPTOR", "=== INTERCEPTOR DEBUG ===")
            Log.d("AUTH_INTERCEPTOR", "URL da requisição: ${originalRequest.url}")
            Log.d("AUTH_INTERCEPTOR", "Método: ${originalRequest.method}")
            Log.d("AUTH_INTERCEPTOR", "Token recuperado do SessionManager: $token")
            
            val newRequest = if (token != null) {
                Log.d("AUTH_INTERCEPTOR", "✅ TOKEN ENCONTRADO - Adicionando header Authorization")
                Log.d("AUTH_INTERCEPTOR", "Header que será enviado: Authorization: $token")
                
                originalRequest.newBuilder()
                    .addHeader("Authorization", token)
                    .build()
            } else {
                Log.w("AUTH_INTERCEPTOR", "⚠️ TOKEN NÃO ENCONTRADO - Requisição sem autenticação")
                originalRequest
            }
            
            // Log dos headers finais da requisição
            Log.d("AUTH_INTERCEPTOR", "Headers da requisição final:")
            newRequest.headers.forEach { header ->
                Log.d("AUTH_INTERCEPTOR", "  ${header.first}: ${header.second}")
            }
            Log.d("AUTH_INTERCEPTOR", "=== FIM DEBUG ===")
            
            chain.proceed(newRequest)
        }
    }

    private fun createOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(createAuthInterceptor(context))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun getInstance(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Manter compatibilidade com código existente
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
