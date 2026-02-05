package com.jesusvilla.pokemon.challenge.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = Color(0xFFE11D2E),
    secondary = Color(0xFF111111),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFE11D2E)
)

@Composable
fun PokemonChallengeTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as? android.app.Activity)?.window
        if (window != null) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }

    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}
