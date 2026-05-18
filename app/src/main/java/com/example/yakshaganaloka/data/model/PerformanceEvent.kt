package com.example.yakshaganaloka.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PerformanceEvent(
    val id: String = "",
    val mela_id: String = "",
    val mela_name: String = "",
    val venue_name: String = "",
    val address: String = "",
    val district: String = "",
    val event_date: String = "",
    val start_time: String = "",
    val end_time: String = "",
    val main_story: String = "",
    val artist_ids: List<String> = emptyList(),
    val entry_free: Boolean = true,
    val ticket_price_inr: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val poster_image_url: String = "",
    val special_notes: String = ""
)

@Serializable
data class PerformanceEventList(
    val performance_events: List<PerformanceEvent>
)
