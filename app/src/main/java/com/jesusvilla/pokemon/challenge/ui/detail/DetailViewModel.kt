package com.jesusvilla.pokemon.challenge.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusvilla.pokemon.challenge.data.repo.PokemonRepository
import com.jesusvilla.pokemon.challenge.domain.model.PokemonDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Error(val message: String) : DetailUiState
    data class Success(val detail: PokemonDetail, val isFavorite: Boolean) : DetailUiState
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repo: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialId = savedStateHandle.get<Int>("id") ?: 1

    private val _state = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    init { load(initialId) }

    fun load(id: Int) {
        viewModelScope.launch {
            _state.value = DetailUiState.Loading
            runCatching { repo.getDetail(id) }
                .onSuccess { detail ->
                    repo.isFavorite(detail.id)
                        .onEach { fav -> _state.value = DetailUiState.Success(detail, fav) }
                        .launchIn(this)
                }
                .onFailure { e ->
                    _state.value = DetailUiState.Error(e.message ?: "Error loading detail")
                }
        }
    }

    fun toggleFavorite() {
        val s = state.value
        if (s is DetailUiState.Success) {
            viewModelScope.launch {
                repo.toggleFavorite(
                    id = s.detail.id,
                    name = s.detail.name,
                    spriteUrl = s.detail.imageUrl,
                    makeFavorite = !s.isFavorite
                )
            }
        }
    }
}
