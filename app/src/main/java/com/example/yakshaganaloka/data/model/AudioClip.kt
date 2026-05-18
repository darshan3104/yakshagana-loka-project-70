package com.example.yakshaganaloka.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AudioClip(
    val id: String = "",
    val title_english: String = "",
    val title_kannada: String = "",
    val artist_name: String = "",
    val artist_id: String = "",
    val category: String = "",
    val prasanga: String = "",
    val duration_seconds: Int = 0,
    val audio_url: String = "",
    val thumbnail_url: String = "",
    val transcript_kannada: String = "",
    val transcript_english: String = "",
    val tags: List<String> = emptyList()
)

@Serializable
data class AudioClipList(
    val audio_clips: List<AudioClip>
)
