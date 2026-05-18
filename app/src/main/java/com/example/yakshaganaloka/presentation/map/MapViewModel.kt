package com.example.yakshaganaloka.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yakshaganaloka.data.model.PerformanceEvent
import com.example.yakshaganaloka.domain.usecase.GetTonightShowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getTonightShowsUseCase: GetTonightShowsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        loadTonightShows()
    }

    fun loadTonightShows() {
        viewModelScope.launch {
            _uiState.value = MapUiState.Loading
            getTonightShowsUseCase().collect { result ->
                result.onSuccess { events ->
                    _uiState.value = MapUiState.Success(events)
                }.onFailure { error ->
                    _uiState.value = MapUiState.Error(error.message ?: "Unknown error")
                }
            }
        }
    }
}

sealed class MapUiState {
    object Loading : MapUiState()
    data class Success(val events: List<PerformanceEvent>) : MapUiState()
    data class Error(val message: String) : MapUiState()
}
