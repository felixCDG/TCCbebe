package com.example.tccbebe.repository

import android.content.Context
import com.example.tccbebe.model.VideoChamadaTokenRequest
import com.example.tccbebe.model.VideoChamadaTokenResponse
import com.example.tccbebe.service.AuthenticatedConexao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoChamadaRepository(private val context: Context) {
    
    private val authenticatedConexao = AuthenticatedConexao(context)
    private val apiService = authenticatedConexao.getVideoChamadaService()
    
    suspend fun gerarTokenVideoChamada(roomName: String): Result<VideoChamadaTokenResponse> = withContext(Dispatchers.IO) {
        try {
            val request = VideoChamadaTokenRequest(room = roomName)
            val response = apiService.gerarTokenVideoChamada(request)
            
            if (response.isSuccessful) {
                val tokenResponse = response.body()
                if (tokenResponse != null) {
                    Result.success(tokenResponse)
                } else {
                    Result.failure(Exception("Resposta vazia da API"))
                }
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Token de autenticação inválido"
                    403 -> "Acesso negado para videochamada"
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
