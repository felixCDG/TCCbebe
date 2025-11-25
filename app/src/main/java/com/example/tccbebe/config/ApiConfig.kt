package com.example.tccbebe.config

object ApiConfig {
    // Configure aqui a URL do seu backend
    const val BASE_URL = "https://backend-sosbaby.onrender.com/v1/sosbaby/" // Para emulador Android
    // const val BASE_URL = "http://192.168.1.100:3030/v1/sosbaby/" // Para dispositivo físico na mesma rede
    // const val BASE_URL = "https://seu-backend.herokuapp.com/v1/sosbaby/" // Para produção
    
    const val TIMEOUT_SECONDS = 30L
}
