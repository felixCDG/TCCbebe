package com.example.tccbebe.service

import com.example.tccbebe.model.ChatIARequest
import com.example.tccbebe.model.ChatIAResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatIAApiService {
    
    @Headers("Content-Type: application/json")
    @POST("ia/generate/chat")
    suspend fun enviarPerguntaIA(@Body request: ChatIARequest): Response<ChatIAResponse>
}
