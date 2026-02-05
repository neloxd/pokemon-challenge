package com.jesusvilla.pokemon.challenge.data.remote.dto

data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val abilities: List<AbilitySlotDto>,
    val stats: List<StatSlotDto>,
    val types: List<TypeSlotDto>,
    val moves: List<MoveSlotDto>,
    val sprites: SpritesDto
)

data class AbilitySlotDto(
    val ability: NamedApiResource,
    val is_hidden: Boolean,
    val slot: Int
)

data class StatSlotDto(
    val base_stat: Int,
    val effort: Int,
    val stat: NamedApiResource
)

data class TypeSlotDto(
    val slot: Int,
    val type: NamedApiResource
)

data class MoveSlotDto(
    val move: NamedApiResource
)

data class SpritesDto(
    val front_default: String?,
    val other: OtherSpritesDto?
)

data class OtherSpritesDto(
    val `official-artwork`: OfficialArtworkDto?
)

data class OfficialArtworkDto(
    val front_default: String?
)
