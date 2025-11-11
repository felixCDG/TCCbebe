package com.example.tccbebe.network

import com.example.tccbebe.config.ApiConfig
import com.example.tccbebe.service.ChatApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val requestWithHeaders = originalRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build()
            chain.proceed(requestWithHeaders)
        }
        .connectTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val chatApiService: ChatApiService = retrofit.create(ChatApiService::class.java)
    
    // Método para obter instância autenticada (se necessário no futuro)
    fun getAuthenticatedInstance(token: String? = null): ChatApiService {
        val authenticatedClient = if (token != null) {
            okHttpClient.newBuilder()
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val authenticatedRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .build()
                    chain.proceed(authenticatedRequest)
                }
                .build()
        } else {
            okHttpClient
        }
        
        val authenticatedRetrofit = Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(authenticatedClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            
        return authenticatedRetrofit.create(ChatApiService::class.java)
    }
}
