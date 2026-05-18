package com.example.yakshaganaloka.data.local

import android.content.Context
import com.example.yakshaganaloka.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalJsonDataSource @Inject constructor(
    private val context: Context
) {
    private val json = Json { 
        ignoreUnknownKeys = true 
        coerceInputValues = true
    }

    private val cache = mutableMapOf<String, Any>()

    suspend fun loadArtists(): List<Artist> = loadData("artists.json") {
        json.decodeFromString<ArtistList>(it).artists
    }

    suspend fun loadVeshas(): List<Vesha> = loadData("veshas.json") {
        json.decodeFromString<VeshaList>(it).veshas
    }

    suspend fun loadMelas(): List<Mela> = loadData("melas.json") {
        json.decodeFromString<MelaList>(it).melas
    }

    suspend fun loadPerformanceEvents(): List<PerformanceEvent> = loadData("performance_events.json") {
        json.decodeFromString<PerformanceEventList>(it).performance_events
    }

    suspend fun loadAudioClips(): List<AudioClip> = loadData("audio_clips.json") {
        json.decodeFromString<AudioClipList>(it).audio_clips
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <T> loadData(fileName: String, parser: (String) -> T): T {
        return withContext(Dispatchers.IO) {
            if (cache.containsKey(fileName)) {
                return@withContext cache[fileName] as T
            }
            try {
                val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
                val data = parser(jsonString)
                cache[fileName] = data as Any
                android.util.Log.d("JSON_DEBUG", "Successfully loaded $fileName")
                if (data is List<*>) {
                    android.util.Log.d("JSON_DEBUG", "Parsed items from $fileName: ${data.size}")
                }
                data
            } catch (e: Exception) {
                android.util.Log.e("JSON_ERROR", "Failed parsing $fileName", e)
                throw e
            }
        }
    }
}
