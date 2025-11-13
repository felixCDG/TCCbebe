package com.example.tccbebe.model


import com.google.gson.annotations.SerializedName
import java.util.Date

data class CadastroRotina(
    val id_rotina: Int,
    val titulo: String,
    val cor: String,
    @SerializedName("id_user") var idUser: Int = 0,
    @SerializedName("id_itens") var idItens: List<Int> = emptyList(),
)

data class ResponseCadastroRotina(
    val status: Boolean,
    val status_code: Int,
    val message: String,
    val data: CadastroItemRotina
)