package com.example.yakshaganaloka.data.repository

import com.example.yakshaganaloka.data.model.Message
import com.example.yakshaganaloka.data.model.OpenRouterRequest
import com.example.yakshaganaloka.data.remote.OpenRouterApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpenRouterRepository @Inject constructor(
    private val openRouterApiService: OpenRouterApiService
) {

    suspend fun explainDialogue(prasangaName: String, transcript: String): String = withContext(Dispatchers.IO) {
        val request = OpenRouterRequest(
            model = "openai/gpt-4o-mini",
            messages = listOf(
                Message(
                    role = "system",
                    content = "You are an expert in Yakshagana culture, prasangas, and traditional Kannada performance arts. Keep explanations simple, educational, and accessible. Maximum 80 words."
                ),
                Message(
                    role = "user",
                    content = "Explain the significance of this dialogue from the Prasanga \"$prasangaName\":\n\"$transcript\""
                )
            )
        )

        try {
            android.util.Log.d("OPENROUTER", "Request started")
            val response = openRouterApiService.getChatCompletion(request)
            
            if (response.isSuccessful) {
                android.util.Log.d("OPENROUTER", "Response received")
                val body = response.body()
                val content = body?.choices?.firstOrNull()?.message?.content
                content ?: "No explanation available."
            } else {
                android.util.Log.e("OPENROUTER", "API failed with code: ${response.code()}")
                when (response.code()) {
                    401 -> "Invalid API configuration."
                    403 -> "Permission denied to access the AI service."
                    429 -> "AI service is busy. Please try again."
                    else -> "Unable to generate explanation."
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("OPENROUTER", "API failed", e)
            "Unable to generate explanation due to a network error."
        }
    }
}
