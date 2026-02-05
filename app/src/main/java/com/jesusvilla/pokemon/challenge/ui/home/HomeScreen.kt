package com.jesusvilla.pokemon.challenge.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.hilt.navigation.compose.hiltViewModel
import com.jesusvilla.pokemon.challenge.ui.components.PokemonGridCard

private val PokedexRed = Color(0xFFE11D2E)

@Composable
fun HomeScreen(
    onOpenDetail: (Int) -> Unit,
    vm: HomeViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val items = remember(state.items, state.query, state.sort) { vm.visibleItems() }

    var sortMenuOpen by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(PokedexRed)
            .statusBarsPadding()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pokédex",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchField(
                value = state.query,
                onValueChange = vm::onQueryChange,
                onClear = vm::clearQuery,
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(12.dp))

            Box {
                SortFab(sort = state.sort) { sortMenuOpen = true }

                SortPopup(
                    expanded = sortMenuOpen,
                    sort = state.sort,
                    onDismiss = { sortMenuOpen = false },
                    onSelect = {
                        vm.onSortChange(it)
                        sortMenuOpen = false
                    }
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            when {
                state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.error ?: "Error")
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(items, key = { it.id }) { item ->
                            PokemonGridCard(
                                item = item,
                                onClick = { onOpenDetail(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .heightIn(min = 56.dp),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp,
            lineHeight = 20.sp
        ),
        placeholder = {
            Text(
                text = "Search",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                )
            )
        },
        leadingIcon = { Icon(Icons.Default.Search, null, tint = PokedexRed) },
        trailingIcon = {
            if (value.isNotBlank()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Close, null, tint = PokedexRed)
                }
            }
        },
        shape = RoundedCornerShape(24.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color(0xFF1D1D1D),
            unfocusedTextColor = Color(0xFF1D1D1D),
            cursorColor = PokedexRed
        )
    )
}

@Composable
private fun SortFab(sort: SortOption, onClick: () -> Unit) {
    val label = if (sort == SortOption.Number) "#" else "A"
    Surface(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        color = Color.White,
        tonalElevation = 2.dp,
        shadowElevation = 6.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(label, fontWeight = FontWeight.Black, fontSize = 16.sp)
        }
    }
}

@Composable
private fun SortPopup(
    expanded: Boolean,
    sort: SortOption,
    onDismiss: () -> Unit,
    onSelect: (SortOption) -> Unit
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        Column(Modifier.padding(12.dp)) {
            Text("Sort by:", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            SortRadio("Number", sort == SortOption.Number) { onSelect(SortOption.Number) }
            SortRadio("Name", sort == SortOption.Name) { onSelect(SortOption.Name) }
        }
    }
}

@Composable
private fun SortRadio(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(Modifier.width(8.dp))
        Text(label)
    }
}
