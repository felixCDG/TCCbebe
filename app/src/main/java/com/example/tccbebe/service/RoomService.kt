package com.example.tccbebe.service

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// Simple response model. Backend should return a JSON like { "exists": true }
data class RoomExists(
    val exists: Boolean
)

interface RoomService {
    @GET("room/{name}")
    suspend fun checkRoom(@Path(value = "name", encoded = true) name: String): Response<RoomExists>
}

