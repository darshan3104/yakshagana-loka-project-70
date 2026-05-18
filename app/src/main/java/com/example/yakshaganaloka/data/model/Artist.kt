package com.example.yakshaganaloka.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    val id: String = "",
    val name_kannada: String = "",
    val name_english: String = "",
    val specialization: String = "",
    val district: String = "",
    val years_active: Int = 0,
    val is_legendary: Boolean = false,
    val profile_image_url: String = "",
    val bio_english: String = "",
    val bio_kannada: String = "",
    val mela_id: String = "",
    val vesha_ids: List<String> = emptyList(),
    val social_links: SocialLinks = SocialLinks()
)

@Serializable
data class SocialLinks(
    val youtube: String = "",
    val instagram: String = ""
)

@Serializable
data class ArtistList(
    val artists: List<Artist>
)
