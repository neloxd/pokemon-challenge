package com.jesusvilla.pokemon.challenge.data.remote.dto

data class PokemonSpeciesDto(
    val flavor_text_entries: List<FlavorTextEntryDto>
)

data class FlavorTextEntryDto(
    val flavor_text: String,
    val language: NamedApiResource
)
