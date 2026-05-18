package com.example.yakshaganaloka.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Mela(
    val id: String = "",
    val name: String = "",
    val district: String = "",
    val founded_year: Int = 0,
    val style: String = "",
    val manager_name: String = "",
    val contact_phone: String = "",
    val contact_email: String = "",
    val home_base: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val logo_url: String = "",
    val description: String = "",
    val artist_ids: List<String> = emptyList()
)

@Serializable
data class MelaList(
    val melas: List<Mela>
)
