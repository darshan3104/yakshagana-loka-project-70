package com.example.yakshaganaloka.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import com.example.yakshaganaloka.data.model.*

@Singleton
class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getPerformanceEventsByDate(date: String): List<PerformanceEvent> {
        return firestore.collection("performance_events")
            .whereEqualTo("event_date", date)
            .get()
            .await()
            .toObjects<PerformanceEvent>()
    }

    suspend fun getArtistById(id: String): Artist? {
        return firestore.collection("artists")
            .document(id)
            .get()
            .await()
            .toObject(Artist::class.java)
    }

    suspend fun getArtists(): List<Artist> {
        return firestore.collection("artists")
            .get()
            .await()
            .toObjects<Artist>()
    }

    suspend fun getMelas(): List<Mela> {
        return firestore.collection("melas")
            .get()
            .await()
            .toObjects<Mela>()
    }

    suspend fun getAudioClips(): List<AudioClip> {
        return firestore.collection("audio_clips")
            .get()
            .await()
            .toObjects<AudioClip>()
    }
}
