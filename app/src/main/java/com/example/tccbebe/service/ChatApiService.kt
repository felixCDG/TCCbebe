package com.example.tccbebe.service

import com.example.tccbebe.model.*
import retrofit2.Response
import retrofit2.http.*

interface ChatApiService {
    
    // ==================== CHAT ENDPOINTS ====================
    
    @GET("chats")
    suspend fun getAllChats(): Response<ApiResponse<List<Chat>>>
    
    @GET("chat/{id}")
    suspend fun getChatById(@Path("id") chatId: String): Response<ApiResponse<Chat>>
    
    @POST("chat/cadastro")
    suspend fun criarChat(@Body chatRequest: CriarChatRequest): Response<ApiResponse<Chat>>
    
    // ==================== MESSAGE ENDPOINTS ====================
    
    @GET("messages")
    suspend fun getAllMessages(): Response<ApiResponse<List<Mensagem>>>
    
    @GET("message/{id}")
    suspend fun getMessageById(@Path("id") messageId: String): Response<ApiResponse<Mensagem>>
    
    @Headers("Content-Type: application/json")
    @POST("message/send")
    suspend fun enviarMensagem(@Body mensagemRequest: EnviarMensagemRequest): Response<ApiResponse<Mensagem>>
    
    // ==================== USER ENDPOINTS (para contatos) ====================
    
    @GET("users")
    suspend fun getAllUsers(): Response<ApiResponse<List<Usuario>>>
    
    @GET("user/{id}")
    suspend fun getUserById(@Path("id") userId: String): Response<ApiResponse<Usuario>>
    
    // ==================== DOCTOR ENDPOINTS (para contatos médicos) ====================
    
    @GET("doctors")
    suspend fun getAllDoctors(): Response<ApiResponse<List<Medico>>>
    
    @GET("doctor/{id}")
    suspend fun getDoctorById(@Path("id") doctorId: String): Response<ApiResponse<Medico>>
    
    // ==================== MENSAGENS POR CHAT ====================
    
    @GET("chat/{chatId}/messages")
    suspend fun getMessagesByChat(@Path("chatId") chatId: String): Response<ApiResponse<List<Mensagem>>>
}

// CriarChatRequest moved to model package to avoid duplication

data class Medico(
    val id: String,
    val nome: String,
    val email: String,
    val especialidade: String = "",
    val crm: String = "",
    val telefone: String = ""
)
