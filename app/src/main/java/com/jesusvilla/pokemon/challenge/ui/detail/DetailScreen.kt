package com.jesusvilla.pokemon.challenge.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.jesusvilla.pokemon.challenge.domain.model.PokemonDetail
import com.jesusvilla.pokemon.challenge.ui.theme.PokemonTypeColors

@Composable
fun DetailScreen(
    id: Int,
    onBack: () -> Unit,
    onNavigateTo: (Int) -> Unit,
    vm: DetailViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(id) { vm.load(id) }

    when (val s = state) {
        DetailUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        is DetailUiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(s.message)
        }
        is DetailUiState.Success -> DetailContent(
            detail = s.detail,
            onBack = onBack,
            onPrev = { if (s.detail.id > 1) onNavigateTo(s.detail.id - 1) },
            onNext = { onNavigateTo(s.detail.id + 1) },
        )
    }
}

@Composable
private fun DetailContent(
    detail: PokemonDetail,
    onBack: () -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit,
) {
    val mainType = detail.types.firstOrNull().orEmpty()
    val bg = PokemonTypeColors.colorFor(mainType)

    Box(Modifier.fillMaxSize().background(bg)) {
        Spacer(Modifier.height(24.dp))

        Row(
            Modifier.fillMaxWidth().padding(top = 24.dp, start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = detail.name.replaceFirstChar { it.uppercase() },
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = formatDex(detail.id),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.width(6.dp))
        }

        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 96.dp)
                .height(280.dp)
        ) {
            IconButton(onClick = onPrev, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ChevronLeft, null, tint = Color.White)
            }
            AsyncImage(
                model = detail.imageUrl,
                contentDescription = detail.name,
                modifier = Modifier.align(Alignment.Center).size(230.dp)
            )
            IconButton(onClick = onNext, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(Icons.Default.ChevronRight, null, tint = Color.White)
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.62f),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            shadowElevation = 10.dp
        ) {
            Column(
                Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    detail.types.take(2).forEach { t -> TypeChip(type = t) }
                }

                Spacer(Modifier.height(10.dp))

                Text("About", color = bg, style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(10.dp))
                AboutRow(detail)

                Spacer(Modifier.height(12.dp))

                if (detail.description.isNotBlank()) {
                    Text(
                        text = detail.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF444444)
                    )
                    Spacer(Modifier.height(14.dp))
                }

                Text("Base Stats", color = bg, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(10.dp))
                BaseStats(detail = detail, accent = bg)
            }
        }
    }
}

@Composable
private fun TypeChip(type: String) {
    val c = PokemonTypeColors.colorFor(type)
    Surface(color = c, shape = RoundedCornerShape(999.dp)) {
        Text(
            text = type.replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = Color.White,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun AboutRow(detail: PokemonDetail) {
    val kg = detail.weightHg / 10f
    val m = detail.heightDm / 10f
    val moves = detail.movesPreview.joinToString("\n") {
        it.replace("-", " ").replaceFirstChar { c -> c.uppercase() }
    }

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AboutCell("Weight", String.format("%.1f kg", kg), Modifier.weight(1f))
        Divider(Modifier.height(48.dp).width(1.dp), color = Color(0xFFE8E8E8))
        AboutCell("Height", String.format("%.1f m", m), Modifier.weight(1f))
        Divider(Modifier.height(48.dp).width(1.dp), color = Color(0xFFE8E8E8))
        AboutCell("Moves", if (moves.isBlank()) "-" else moves, Modifier.weight(1f))
    }
}

@Composable
private fun AboutCell(title: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF222222))
        Spacer(Modifier.height(4.dp))
        Text(title, style = MaterialTheme.typography.labelSmall, color = Color(0xFF8A8A8A))
    }
}

@Composable
private fun BaseStats(detail: PokemonDetail, accent: Color) {
    val max = 255f

    fun label(k: PokemonDetail.StatKey) = when (k) {
        PokemonDetail.StatKey.HP -> "HP"
        PokemonDetail.StatKey.ATK -> "ATK"
        PokemonDetail.StatKey.DEF -> "DEF"
        PokemonDetail.StatKey.SATK -> "SATK"
        PokemonDetail.StatKey.SDEF -> "SDEF"
        PokemonDetail.StatKey.SPD -> "SPD"
    }

    Column(Modifier.fillMaxWidth()) {
        detail.stats.sortedBy { it.key.ordinal }.forEach { st ->
            Row(
                Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label(st.key),
                    color = accent,
                    modifier = Modifier.width(46.dp),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "%03d".format(st.base),
                    modifier = Modifier.width(40.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF333333)
                )
                LinearProgressIndicator(
                    progress = (st.base / max).coerceIn(0f, 1f),
                    modifier = Modifier
                        .height(8.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(999.dp)),
                    color = accent,
                    trackColor = accent.copy(alpha = 0.18f)
                )
            }
        }
    }
}

private fun formatDex(id: Int) = "#%03d".format(id)
