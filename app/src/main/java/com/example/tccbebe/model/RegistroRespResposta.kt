package com.example.tccbebe.model

import com.google.gson.annotations.SerializedName

data class RegistroRespData(
    val id_responsavel: Int,
    val nome: String,
    val data_nascimento: String,
    val cpf: String ,
    val telefone: String,
    val arquivo: String,
    val cartao_medico: String,
    val cep: String,
    @SerializedName("id_sexo") var idSexo: Int,
    val id_user: Int
)

data class ResponseRegistroResp(
    val status: Boolean,
    val status_code: Int,
    val message: String,
    val data: RegistroRespData
)