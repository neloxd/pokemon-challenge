package com.jesusvilla.pokemon.challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jesusvilla.pokemon.challenge.ui.nav.AppNavGraph
import com.jesusvilla.pokemon.challenge.ui.theme.PokemonChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonChallengeTheme {
                AppNavGraph()
            }
        }
    }
}
