package com.example.yakshaganaloka.domain.usecase

import com.example.yakshaganaloka.data.model.Artist
import com.example.yakshaganaloka.data.model.Vesha
import com.example.yakshaganaloka.data.repository.ArtistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class ArtistProfile(
    val artist: Artist,
    val veshas: List<Vesha>
)

class GetArtistProfileUseCase @Inject constructor(
    private val repository: ArtistRepository
) {
    fun invoke(artistId: String): Flow<Result<ArtistProfile>> {
        return repository.getArtistById(artistId).map { result ->
            result.map { artist ->
                val veshas = repository.getVeshasForArtist(artistId)
                ArtistProfile(artist, veshas)
            }
        }
    }
}
