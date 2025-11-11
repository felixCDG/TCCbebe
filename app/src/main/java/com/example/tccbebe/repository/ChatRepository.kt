package com.example.tccbebe.repository

import android.content.Context
import com.example.tccbebe.model.*
import com.example.tccbebe.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRepository(private val context: Context) {
    
    private val apiService = RetrofitClient.chatApiService
    
    // ==================== CHAT METHODS ====================
    
    suspend fun getAllChats(): Result<List<Chat>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllChats()
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true && apiResponse.data != null) {
                    Result.success(apiResponse.data)
                } else {
                    Result.failure(Exception(apiResponse?.message ?: "Erro desconhecido"))
                }
            } else {
                Result.failure(Exception("Erro HTTP: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de rede: ${e.message}"))
        }
    }
    
    suspend fun getChatById(chatId: String): Result<Chat> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getChatById(chatId)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true && apiResponse.data != null) {
                    Result.success(apiResponse.data)
                } else {
                    Result.failure(Exception(apiResponse?.message ?: "Chat não encontrado"))
                }
            } else {
                Result.failure(Exception("Erro HTTP: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de rede: ${e.message}"))
        }
    }
    
    // ==================== MESSAGE METHODS ====================
    
    suspend fun getAllMessages(): Result<List<Mensagem>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllMessages()
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true && apiResponse.data != null) {
                    Result.success(apiResponse.data)
                } else {
                    Result.failure(Exception(apiResponse?.message ?: "Erro desconhecido"))
                }
            } else {
                Result.failure(Exception("Erro HTTP: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de rede: ${e.message}"))
        }
    }
    
    suspend fun getMessagesByChat(chatId: String): Result<List<Mensagem>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllMessages()
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true && apiResponse.data != null) {
                    val mensagensDoChat = apiResponse.data.filter { it.id_chat == chatId }
                    Result.success(mensagensDoChat)
                } else {
                    Result.failure(Exception(apiResponse?.message ?: "Erro desconhecido"))
                }
            } else {
                Result.failure(Exception("Erro HTTP: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de rede: ${e.message}"))
        }
    }
    
    suspend fun enviarMensagem(conteudo: String, chatId: String, userId: String): Result<Mensagem> = withContext(Dispatchers.IO) {
        try {
            val request = EnviarMensagemRequest(conteudo, chatId, userId)
            val response = apiService.enviarMensagem(request)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true && apiResponse.data != null) {
                    Result.success(apiResponse.data)
                } else {
                    Result.failure(Exception(apiResponse?.message ?: "Erro ao enviar mensagem"))
                }
            } else {
                Result.failure(Exception("Erro HTTP: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de rede: ${e.message}"))
        }
    }
    
    // ==================== CONTATOS METHODS ====================
    
    suspend fun getAllContatos(): Result<List<Contato>> = withContext(Dispatchers.IO) {
        try {
            // Buscar usuários e médicos para formar a lista de contatos
            val usuariosResponse = apiService.getAllUsers()
            val medicosResponse = apiService.getAllDoctors()
            
            val contatos = mutableListOf<Contato>()
            
            // Adicionar usuários como contatos
            if (usuariosResponse.isSuccessful) {
                val usuariosApiResponse = usuariosResponse.body()
                if (usuariosApiResponse?.success == true && usuariosApiResponse.data != null) {
                    contatos.addAll(usuariosApiResponse.data.map { usuario ->
                        Contato(
                            id = usuario.id,
                            nome = usuario.nome,
                            email = usuario.email,
                            tipo = usuario.tipo,
                            status = "offline" // Status padrão
                        )
                    })
                }
            }
            
            // Adicionar médicos como contatos
            if (medicosResponse.isSuccessful) {
                val medicosApiResponse = medicosResponse.body()
                if (medicosApiResponse?.success == true && medicosApiResponse.data != null) {
                    contatos.addAll(medicosApiResponse.data.map { medico ->
                        Contato(
                            id = medico.id,
                            nome = medico.nome,
                            email = medico.email,
                            tipo = "Dr. ${medico.especialidade}",
                            status = "online" // Médicos sempre online por padrão
                        )
                    })
                }
            }
            
            if (contatos.isNotEmpty()) {
                Result.success(contatos)
            } else {
                Result.failure(Exception("Nenhum contato encontrado"))
            }
            
        } catch (e: Exception) {
            Result.failure(Exception("Erro de rede: ${e.message}"))
        }
    }
}
