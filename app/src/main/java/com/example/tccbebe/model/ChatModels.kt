package com.example.tccbebe.model

data class Contato(
    val id: String,
    val nome: String,
    val email: String = "",
    val tipo: String,
    val status: String = "offline"
)

data class Chat(
    val id: String,
    val nome: String,
    val ultimaMensagem: String = "",
    val horario: String = "",
    val participantes: List<String> = emptyList()
)

data class Mensagem(
    val id: String,
    val conteudo: String,
    val id_chat: String,
    val id_user: String,
    val created_at: String,
    val remetente: String = "",
    val horario: String = ""
)

data class Usuario(
    val id: String,
    val nome: String,
    val email: String,
    val tipo: String = "usuario"
)

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String
)

data class EnviarMensagemRequest(
    val conteudo: String,
    val id_chat: String,
    val id_user: String
)

data class CriarChatRequest(
    val nome: String,
    val participantes: List<String>
)
