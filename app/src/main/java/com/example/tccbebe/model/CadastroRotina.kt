package com.example.tccbebe.model


import com.google.gson.annotations.SerializedName
import java.util.Date

data class CadastroRotina(
    val id_rotina: Int,
    val titulo_rotina: String,
    val cor: String,
    @SerializedName("id_user") var idUser: Int = 0,
    val titulo_item: String,
    val descricao: String,
    val hora: String,
    val data_rotina: String,
)

data class ResponseCadastroRotina(
    val status: Boolean,
    val status_code: Int,
    val message: String,
    val data: CadastroRotina
)

