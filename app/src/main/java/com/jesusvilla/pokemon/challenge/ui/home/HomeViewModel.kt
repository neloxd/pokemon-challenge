package com.jesusvilla.pokemon.challenge.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusvilla.pokemon.challenge.data.repo.PokemonRepository
import com.jesusvilla.pokemon.challenge.domain.model.PokemonListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortOption { Number, Name }

data class HomeUiState(
    val loading: Boolean = true,
    val error: String? = null,
    val query: String = "",
    val sort: SortOption = SortOption.Number,
    val items: List<PokemonListItem> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching { repo.getAllPokemon() }
                .onSuccess { list ->
                    repo.favorites()
                        .onEach { favIds ->
                            val merged = list.map { it.copy(isFavorite = favIds.contains(it.id)) }
                            _state.update { it.copy(loading = false, items = merged, error = null) }
                        }
                        .launchIn(this)
                }
                .onFailure { e ->
                    _state.update { it.copy(loading = false, error = e.message ?: "Error") }
                }
        }
    }

    fun onQueryChange(q: String) = _state.update { it.copy(query = q) }
    fun onSortChange(s: SortOption) = _state.update { it.copy(sort = s) }
    fun clearQuery() = _state.update { it.copy(query = "") }

    fun visibleItems(): List<PokemonListItem> {
        val s = _state.value
        val query = s.query.trim()

        val filtered = if (query.isEmpty()) {
            s.items
        } else {
            val normalized = query.removePrefix("#").trim()
            val asNumber = normalized.toIntOrNull()
            if (asNumber != null) s.items.filter { it.id == asNumber }
            else s.items.filter { it.name.contains(query, ignoreCase = true) }
        }

        return when (s.sort) {
            SortOption.Number -> filtered.sortedBy { it.id }
            SortOption.Name -> filtered.sortedBy { it.name }
        }
    }

    fun toggleFavorite(item: PokemonListItem) {
        viewModelScope.launch {
            repo.toggleFavorite(
                id = item.id,
                name = item.name,
                spriteUrl = item.spriteUrl,
                makeFavorite = !item.isFavorite
            )
        }
    }
}
