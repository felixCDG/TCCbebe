package com.example.tccbebe.model

data class ChatIARequest(
    val question: String
)

data class ChatIAResponse(
    val message: String,
    val status_code: Int,
    val IA_response: String,
    val history: List<HistoryItem>? = null
)

data class HistoryItem(
    val remetente: String, // "user" ou "IA"
    val pergunta: String? = null,
    val resposta: String? = null
)

data class ChatIAApiResponse(
    val success: Boolean,
    val data: ChatIAResponse?,
    val message: String
)
