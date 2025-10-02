package com.example.tccbebe.model

data class RegistroResp(
    val id_responsavel: Int = 0,
    val nome: String = "",
    val data_nascimento: String = "",
    val cpf: String = "",
    val telefone: String = "",
    val cartao_medico: String = "",
    val cep: String = "",
)
