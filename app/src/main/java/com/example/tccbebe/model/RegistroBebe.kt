package com.example.tccbebe.model

import com.google.gson.annotations.SerializedName

data class RegistroBebe(
    val id_bebe: Int = 0,
    val nome: String = "",
    val data_nascimento: String = "",
    val peso: String = "",
    val altura: String = "",
    val certidao_nascimento: String = "",
    val cpf: String = "",
    val imagem_certidao: String = "",
    val cartao_medico: String = "",
    @SerializedName("id_sexo") var idSexo: Int = 0,
    @SerializedName("id_sangue") var idSangue: Int = 0,

)