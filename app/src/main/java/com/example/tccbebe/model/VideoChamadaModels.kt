package com.example.tccbebe.model

data class VideoChamadaTokenRequest(
    val room: String
)

data class UserData(
    val id: Int,
    val nome: String,
    val tipo: Int
)

data class TokenData(
    val token: String,
    val indentity: String,
    val Room: String
)

data class VideoChamadaTokenResponse(
    val status_code: Int,
    val message: String,
    val userData: UserData,
    val token: TokenData
)
