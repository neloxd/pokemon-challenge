package com.jesusvilla.pokemon.challenge.ui.theme

import androidx.compose.ui.graphics.Color

object PokemonTypeColors {
    private val map = mapOf(
        "grass" to Color(0xFF63BC5A),
        "poison" to Color(0xFFB567CE),
        "fire" to Color(0xFFFF9D55),
        "water" to Color(0xFF5090D6),
        "bug" to Color(0xFF91C12F),
        "normal" to Color(0xFF919AA2),
        "electric" to Color(0xFFF4D23C),
        "ground" to Color(0xFFD97845),
        "fairy" to Color(0xFFEC8FE6),
        "fighting" to Color(0xFFCE416B),
        "psychic" to Color(0xFFFA7179),
        "rock" to Color(0xFFC5B78C),
        "ghost" to Color(0xFF5269AD),
        "ice" to Color(0xFF73CEC0),
        "dragon" to Color(0xFF0B6DC3),
        "dark" to Color(0xFF5A5465),
        "steel" to Color(0xFF5A8EA2),
        "flying" to Color(0xFF89AAE3)
    )
    fun colorFor(type: String): Color = map[type.lowercase()] ?: Color(0xFF9E9E9E)
}
