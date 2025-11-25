package com.example.tccbebe.model

import com.google.gson.annotations.SerializedName

data class ViewRoutinesResponse(
    @SerializedName("status_code") val status_code: Int,
    val message: String,
    val data: List<CadastroRotina>
)

