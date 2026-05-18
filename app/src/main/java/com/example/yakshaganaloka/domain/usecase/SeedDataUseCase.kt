package com.example.yakshaganaloka.domain.usecase

import com.example.yakshaganaloka.data.local.LocalJsonDataSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SeedDataUseCase @Inject constructor(
    private val localJsonDataSource: LocalJsonDataSource,
    private val firestore: FirebaseFirestore
) {
    suspend fun invoke() {
        seedCollection("artists") { localJsonDataSource.loadArtists() }
        seedCollection("veshas") { localJsonDataSource.loadVeshas() }
        seedCollection("melas") { localJsonDataSource.loadMelas() }
        seedCollection("performance_events") { localJsonDataSource.loadPerformanceEvents() }
        seedCollection("audio_clips") { localJsonDataSource.loadAudioClips() }
    }

    private suspend fun seedCollection(collectionName: String, loader: suspend () -> List<Any>) {
        val count = firestore.collection(collectionName).count().get(com.google.firebase.firestore.AggregateSource.SERVER).await().count
        if (count == 0L) {
            val data = loader()
            val batch = firestore.batch()
            data.forEach { item ->
                // This is a simplified batch write. In production, check for item ID.
                val docId = when (item) {
                    is com.example.yakshaganaloka.data.model.Artist -> item.id
                    is com.example.yakshaganaloka.data.model.Vesha -> item.id
                    is com.example.yakshaganaloka.data.model.Mela -> item.id
                    is com.example.yakshaganaloka.data.model.PerformanceEvent -> item.id
                    is com.example.yakshaganaloka.data.model.AudioClip -> item.id
                    else -> null
                }
                if (docId != null) {
                    batch.set(firestore.collection(collectionName).document(docId), item)
                }
            }
            batch.commit().await()
        }
    }
}
