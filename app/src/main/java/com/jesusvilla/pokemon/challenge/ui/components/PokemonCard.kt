package com.jesusvilla.pokemon.challenge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jesusvilla.pokemon.challenge.domain.model.PokemonListItem

private val CardGray = Color(0xFFEFEFEF)
private val NumberGray = Color(0xFF9E9E9E)
private val TextDark = Color(0xFF1D1D1D)

@Composable
fun PokemonGridCard(
    item: PokemonListItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(16.dp)

    val bottomGrayShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)

    Card(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(104f / 108f),
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(cardShape)
        ) {

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(bottomGrayShape)
                    .background(CardGray)
            )

            Text(
                text = "#%03d".format(item.id),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 10.dp),
                color = NumberGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            AsyncImage(
                model = item.spriteUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .size(72.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Fit
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(start = 8.dp, end = 8.dp, top = 24.dp, bottom = 4.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = item.name.prettyPokemonName(),
                    textAlign = TextAlign.Center,
                    color = TextDark,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * @return  // "mr-mime" -> "Mr-mime" (capitalize each segment)
 */
private fun String.prettyPokemonName(): String {
    return split("-").joinToString("-") { part ->
        part.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}
