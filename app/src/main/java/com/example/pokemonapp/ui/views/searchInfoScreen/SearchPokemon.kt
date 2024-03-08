package com.example.pokemonapp.ui.views.searchInfoScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.pokemonapp.model.pokemon.PokemonResult


@Composable
fun PokemonList(
    viewModel: SearchPokemonViewModel,
    modifier: Modifier = Modifier,
    onPokemonSelected: (PokemonResult) -> Unit,
) {
    val pokemonUiState = viewModel.pokemonUiState

    Column(
        modifier = modifier
            //.padding(8.dp)
    ) {

        when (pokemonUiState) {
            is PokemonUiState.Loading -> LoadingScreen()
            is PokemonUiState.Success -> {
                val pokemonList = (pokemonUiState as PokemonUiState.Success).pokemonList
                val searchText by viewModel.searchText.collectAsState()

                SearchBar(
                    pokemonList = pokemonList ?: emptyList(),
                    initialsearchText = searchText,
                    onSearchTextChange = { newText ->
                        viewModel.onSearchTextChange(newText)
                    },
                    onSearchTextSubmit = { pokemon ->
                        onPokemonSelected(pokemon)
                    },
                    modifier = modifier
                )
                PokemonList(
                    pokemonList = pokemonList ?: emptyList(),
                    searchText = searchText,
                    onItemClick = { pokemon ->
                        onPokemonSelected(pokemon)
                    }
                )
            }
            is PokemonUiState.Error -> ErrorScreen()
        }
    }
}

@Composable
fun PokemonList(
    pokemonList: List<PokemonResult>,
    searchText: String = "",
    onItemClick: (PokemonResult) -> Unit
) {
    val filteredPokemonList = if (searchText.isNotBlank()) {
        pokemonList.filter { it.name.contains(searchText, ignoreCase = true) }
    } else {
        pokemonList
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(filteredPokemonList) { pokemon ->
            PokemonListItem(
                pokemon = pokemon,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun PokemonListItem(
    pokemon: PokemonResult,
    onItemClick: (PokemonResult) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(pokemon) }
            .padding(8.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${getIdFromUrl(pokemon.url)}.png"
        Image(
            painter = rememberImagePainter(data = imageUrl, builder = {
                placeholder(androidx.core.R.drawable.ic_call_answer) // Placeholder image while loading
                error(androidx.core.R.drawable.ic_call_answer_video) // Image to display in case of error
            }),
            contentDescription = null, // Content description for accessibility
            modifier = Modifier.size(64.dp), // Size of the image
            contentScale = ContentScale.Crop, // Scale type for the image
        )
        //Spacer(modifier = Modifier.width(16.dp)) // Spacer between image and text
        val pokemonName = pokemon.name.replaceFirstChar {
            it.uppercase()
        }
        Text(
            text = pokemonName,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 64.dp)
                .fillMaxWidth()
        )
    }
}

fun getIdFromUrl(url: String): Int {
    val segments = url.split("/")
    return segments[segments.size - 2].toInt()
}

@Composable
fun SearchBar(
    pokemonList: List<PokemonResult>,
    initialsearchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchTextSubmit: (PokemonResult) -> Unit,
    modifier: Modifier
) {
    var searchText by remember { mutableStateOf(initialsearchText) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText
                onSearchTextChange(newText)
            },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = { Text(text = "Search...") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    val selectedPokemon = pokemonList.find { it.name.equals(searchText, ignoreCase = true) }
                    if (selectedPokemon != null) {
                        // Call the callback to notify that a Pokemon is selected
                        onSearchTextSubmit(selectedPokemon)
                    }
                }
            ),
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        )
    }
}

/**
 * The home screen displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    // Hier können Sie den Ladebildschirminhalt definieren
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    // Hier können Sie den Fehlerbildschirminhalt definieren
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error occurred", color = Color.Red)
    }
}

@Preview
@Composable
fun PreviewSearchBar() {
    SearchBar(pokemonList = listOf(), initialsearchText = "bulbasaur", {}, {}, modifier = Modifier.padding(16.dp))
}
