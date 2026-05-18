package com.example.yakshaganaloka.presentation.home

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
class HomeViewModel @Inject constructor(
    private val getTonightShowsUseCase: GetTonightShowsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadTonightShows()
    }

    fun loadTonightShows() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            getTonightShowsUseCase().collect { result ->
                result.onSuccess { events ->
                    _uiState.value = HomeUiState.Success(events)
                }.onFailure { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Unknown error")
                }
            }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val events: List<PerformanceEvent>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
