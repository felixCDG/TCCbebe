package com.example.tccbebe.model

import com.google.gson.annotations.SerializedName
import java.net.IDN

data class CadastroUser(
    val id_user: Int ,
    val nome_user: String,
    val email: String ,
    val senha: String ,
    val id_tipo: Int = 3,

    )