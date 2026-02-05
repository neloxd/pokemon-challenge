package com.jesusvilla.pokemon.challenge.domain.model

data class PokemonListItem(
    val id: Int,
    val name: String,
    val spriteUrl: String,
    val isFavorite: Boolean
)

data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val heightDm: Int,
    val weightHg: Int,
    val types: List<String>,
    val abilities: List<String>,
    val movesPreview: List<String>,
    val description: String,
    val stats: List<Stat>
) {
    data class Stat(val key: StatKey, val base: Int)
    enum class StatKey { HP, ATK, DEF, SATK, SDEF, SPD }
}
