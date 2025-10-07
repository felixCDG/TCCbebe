package com.example.tccbebe.model

import com.google.gson.annotations.SerializedName

data class RegistroResp(
    val id_responsavel: Int = 0,
    val nome: String = "",
    val data_nascimento: String = "",
    val cpf: String = "",
    val telefone: String = "",
    val arquivo: String = "",
    val cartao_medico: String = "",
    val cep: String = "",
    @SerializedName("id_sexo") var idSexo: Int = 0,
    val id_user: Int = 0

)
