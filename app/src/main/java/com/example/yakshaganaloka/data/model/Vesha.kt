package com.example.yakshaganaloka.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Vesha(
    val id: String = "",
    val artist_id: String = "",
    val role_name_english: String = "",
    val role_name_kannada: String = "",
    val character_type: String = "",
    val prasanga: String = "",
    val costume_description: String = "",
    val images: List<String> = emptyList()
)

@Serializable
data class VeshaList(
    val veshas: List<Vesha>
)
