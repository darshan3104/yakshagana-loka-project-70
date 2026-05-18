package com.example.yakshaganaloka.presentation.mela

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yakshaganaloka.data.model.Mela
import com.example.yakshaganaloka.data.repository.MelaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MelaViewModel @Inject constructor(
    private val melaRepository: MelaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MelaUiState>(MelaUiState.Loading)
    val uiState: StateFlow<MelaUiState> = _uiState.asStateFlow()

    init {
        loadMelas()
    }

    fun loadMelas() {
        viewModelScope.launch {
            _uiState.value = MelaUiState.Loading
            melaRepository.getMelas().collect { result ->
                result.onSuccess { melas ->
                    _uiState.value = MelaUiState.Success(melas)
                }.onFailure { error ->
                    _uiState.value = MelaUiState.Error(error.message ?: "Unknown error")
                }
            }
        }
    }
}

sealed class MelaUiState {
    object Loading : MelaUiState()
    data class Success(val melas: List<Mela>) : MelaUiState()
    data class Error(val message: String) : MelaUiState()
}
