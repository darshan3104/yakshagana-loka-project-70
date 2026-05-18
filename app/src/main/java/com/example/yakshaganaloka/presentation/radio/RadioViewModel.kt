package com.example.yakshaganaloka.presentation.radio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yakshaganaloka.data.model.AudioClip
import com.example.yakshaganaloka.data.repository.AudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RadioViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
    private val openRouterRepository: com.example.yakshaganaloka.data.repository.OpenRouterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RadioUiState>(RadioUiState.Loading)
    val uiState: StateFlow<RadioUiState> = _uiState.asStateFlow()

    private val _currentPlaying = MutableStateFlow<AudioClip?>(null)
    val currentPlaying: StateFlow<AudioClip?> = _currentPlaying.asStateFlow()

    private val _explanation = MutableStateFlow<String?>(null)
    val explanation: StateFlow<String?> = _explanation.asStateFlow()

    init {
        loadAudioClips()
    }

    fun loadAudioClips() {
        viewModelScope.launch {
            _uiState.value = RadioUiState.Loading
            audioRepository.getAudioClips().collect { result ->
                result.onSuccess { clips ->
                    _uiState.value = RadioUiState.Success(clips)
                }.onFailure { error ->
                    _uiState.value = RadioUiState.Error(error.message ?: "Unknown error")
                }
            }
        }
    }

    fun playClip(clip: AudioClip) {
        _currentPlaying.value = clip
        _explanation.value = null
        // TODO: Integrate with ExoPlayer
    }

    fun explainDialogue(clip: AudioClip) {
        viewModelScope.launch {
            _explanation.value = "Generating explanation..."
            val result = openRouterRepository.explainDialogue(clip.prasanga, clip.transcript_english)
            _explanation.value = result
        }
    }
}

sealed class RadioUiState {
    object Loading : RadioUiState()
    data class Success(val clips: List<AudioClip>) : RadioUiState()
    data class Error(val message: String) : RadioUiState()
}
