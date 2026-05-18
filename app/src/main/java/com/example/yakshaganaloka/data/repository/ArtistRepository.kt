package com.example.yakshaganaloka.data.repository

import com.example.yakshaganaloka.data.local.LocalJsonDataSource
import com.example.yakshaganaloka.data.model.Artist
import com.example.yakshaganaloka.data.model.Vesha
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArtistRepository @Inject constructor(
    private val localJsonDataSource: LocalJsonDataSource
) {
    fun getArtists(): Flow<Result<List<Artist>>> = flow {
        try {
            val localArtists = localJsonDataSource.loadArtists()
            emit(Result.success(localArtists))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getArtistById(id: String): Flow<Result<Artist>> = flow {
        try {
            val localArtist = localJsonDataSource.loadArtists().find { it.id == id }
            if (localArtist != null) {
                emit(Result.success(localArtist))
            } else {
                emit(Result.failure(Exception("Artist not found")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun getVeshasForArtist(artistId: String): List<Vesha> {
        return try {
            localJsonDataSource.loadVeshas().filter { it.artist_id == artistId }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
