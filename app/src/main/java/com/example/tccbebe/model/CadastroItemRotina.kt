package com.example.tccbebe.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class CadastroItemRotina(
    val id_item: Int,
    val titulo: String,
    val descricao: String,
    val data_rotina: String,
    val hora: String,
)

data class ResponseCadastroItemRotina(
    val status: Boolean,
    val status_code: Int,
    val message: String,
    val data: CadastroItemRotina
)