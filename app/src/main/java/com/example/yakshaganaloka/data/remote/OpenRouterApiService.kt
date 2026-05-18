package com.example.yakshaganaloka.data.remote

import com.example.yakshaganaloka.data.model.OpenRouterRequest
import com.example.yakshaganaloka.data.model.OpenRouterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenRouterApiService {

    @POST("chat/completions")
    suspend fun getChatCompletion(
        @Body request: OpenRouterRequest
    ): Response<OpenRouterResponse>
}
