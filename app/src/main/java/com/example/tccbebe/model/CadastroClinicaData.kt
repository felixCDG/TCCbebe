package com.example.tccbebe.model

data class CadastroClinicaData(
    val id_clina: Int,
    val nome: String,
    val cnpj: String,
    val telefone: String,
    val email: String,
    val cidade: String,
    val rua: String,
    val bairro: String,
    val numero: String,
    val id_user: Int
)

data class ResponseCadastroClinica(
    val status: Boolean,
    val status_code: Int,
    val message: String,
    val data: CadastroClinicaData
)