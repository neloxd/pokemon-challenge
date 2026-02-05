package com.jesusvilla.pokemon.challenge.data.remote.dto

data class PokemonListResponseDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NamedApiResource>
)
