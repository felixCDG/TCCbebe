package com.example.tccbebe.repository

import android.content.Context
import com.example.tccbebe.model.ChatIARequest
import com.example.tccbebe.model.ChatIAResponse
import com.example.tccbebe.service.AuthenticatedConexao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatIARepository(private val context: Context) {
    
    private val authenticatedConexao = AuthenticatedConexao(context)
    private val apiService = authenticatedConexao.getChatIAService()
    
    suspend fun enviarPerguntaIA(pergunta: String): Result<ChatIAResponse> = withContext(Dispatchers.IO) {
        try {
            val request = ChatIARequest(question = pergunta)
            val response = apiService.enviarPerguntaIA(request)
            
            if (response.isSuccessful) {
                val chatIAResponse = response.body()
                if (chatIAResponse != null) {
                    Result.success(chatIAResponse)
                } else {
                    Result.failure(Exception("Resposta vazia da API"))
                }
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Token de autenticação inválido"
                    403 -> "Acesso negado"
                    500 -> "Erro interno do servidor"
                    else -> "Erro HTTP: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de rede: ${e.message}"))
        }
    }
}
