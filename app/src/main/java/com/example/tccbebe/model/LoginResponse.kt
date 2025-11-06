package com.example.tccbebe.model

data class LoginUserData(
    val id_user: Int,
    val email: String,
    val senha: String,
    val id_tipo: Int = 3
)

data class ResponseLoginUser(
    val status: Boolean,
    val status_code: Int,
    val message: String,
    val data: LoginUserData,
    val token: String
)
