package com.example.tccbebe.service

import com.example.tccbebe.model.VideoChamadaTokenRequest
import com.example.tccbebe.model.VideoChamadaTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface VideoChamadaApiService {
    
    @Headers("Content-Type: application/json")
    @POST("call/token")
    suspend fun gerarTokenVideoChamada(@Body request: VideoChamadaTokenRequest): Response<VideoChamadaTokenResponse>
}
