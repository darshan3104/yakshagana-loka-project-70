package com.example.yakshaganaloka.presentation.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yakshaganaloka.data.model.Artist
import com.example.yakshaganaloka.data.repository.ArtistRepository
import com.example.yakshaganaloka.domain.usecase.ArtistProfile
import com.example.yakshaganaloka.domain.usecase.GetArtistProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val getArtistProfileUseCase: GetArtistProfileUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _directoryUiState = MutableStateFlow<ArtistDirectoryUiState>(ArtistDirectoryUiState.Loading)
    val directoryUiState: StateFlow<ArtistDirectoryUiState> = combine(
        _directoryUiState,
        _searchQuery.debounce(300)
    ) { state, query ->
        if (state is ArtistDirectoryUiState.Success) {
            val filtered = if (query.isEmpty()) {
                state.artists
            } else {
                state.artists.filter {
                    it.name_english.contains(query, ignoreCase = true) ||
                    it.name_kannada.contains(query) ||
                    it.specialization.contains(query, ignoreCase = true)
                }
            }
            ArtistDirectoryUiState.Success(state.artists, filtered)
        } else {
            state
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ArtistDirectoryUiState.Loading)

    private val _profileUiState = MutableStateFlow<ArtistProfileUiState>(ArtistProfileUiState.Loading)
    val profileUiState: StateFlow<ArtistProfileUiState> = _profileUiState.asStateFlow()

    init {
        loadArtists()
    }

    fun loadArtists() {
        viewModelScope.launch {
            _directoryUiState.value = ArtistDirectoryUiState.Loading
            artistRepository.getArtists().collect { result ->
                result.onSuccess { artists ->
                    _directoryUiState.value = ArtistDirectoryUiState.Success(artists, artists)
                }.onFailure { error ->
                    _directoryUiState.value = ArtistDirectoryUiState.Error(error.message ?: "Unknown error")
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun loadArtistProfile(artistId: String) {
        viewModelScope.launch {
            _profileUiState.value = ArtistProfileUiState.Loading
            getArtistProfileUseCase.invoke(artistId).collect { result ->
                result.onSuccess { profile ->
                    _profileUiState.value = ArtistProfileUiState.Success(profile)
                }.onFailure { error ->
                    _profileUiState.value = ArtistProfileUiState.Error(error.message ?: "Unknown error")
                }
            }
        }
    }
}

sealed class ArtistDirectoryUiState {
    object Loading : ArtistDirectoryUiState()
    data class Success(val artists: List<Artist>, val filteredArtists: List<Artist>) : ArtistDirectoryUiState()
    data class Error(val message: String) : ArtistDirectoryUiState()
}

sealed class ArtistProfileUiState {
    object Loading : ArtistProfileUiState()
    data class Success(val profile: ArtistProfile) : ArtistProfileUiState()
    data class Error(val message: String) : ArtistProfileUiState()
}
