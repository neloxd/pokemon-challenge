package com.jesusvilla.pokemon.challenge.data.repo

import com.jesusvilla.pokemon.challenge.domain.model.PokemonDetail
import com.jesusvilla.pokemon.challenge.domain.model.PokemonListItem
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun getAllPokemon(): List<PokemonListItem>
    suspend fun getDetail(id: Int): PokemonDetail

    fun favorites(): Flow<Set<Int>>
    fun isFavorite(id: Int): Flow<Boolean>
    fun favoriteItems(): Flow<List<PokemonListItem>>
    suspend fun toggleFavorite(id: Int, name: String, spriteUrl: String, makeFavorite: Boolean)
}
