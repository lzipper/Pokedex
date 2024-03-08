package com.example.pokemonapp.ui.views.pokemonInfoScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.pokemonapp.model.pokemon.PokemonResponse
import com.example.pokemonapp.model.pokemon.StatsResponse
import com.example.pokemonapp.model.pokemon.TypesResponse
import com.example.pokemonapp.model.species.FlavorTextEntry
import com.example.pokemonapp.model.species.PokemonSpecies

@Composable
fun PokemonInfo(
    viewModel: PokemonInfoViewModel,
    pokemonName: String,
    modifier: Modifier = Modifier,
) {
    val pokemon by viewModel.pokemonResponse.collectAsState()
    val pokemonSpecies by viewModel.pokemonSpecies.collectAsState()
    val pokemonTypeWeakness  by viewModel.pokemonWeaknessList.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getPokemonByName(pokemonName)
        viewModel.getPokemonSpeciesByName(pokemonName)
    }

    LaunchedEffect(pokemon) {
        if (pokemon != null) {
            viewModel.getAllWeaknessesForPokemon()
        }
    }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow)
            .padding(16.dp)
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            if (error) {
                Text(
                    text = "Error loading Pokemon",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                PokemonDetailedInformation(
                    pokemonResponse = pokemon,
                    pokemonSpecies = pokemonSpecies,
                    pokemonTypeWeakness = pokemonTypeWeakness,
                    onPreviousClick = {},
                    onNextClick = {},
                )
            }
        }
    }
}

@Composable
fun PokemonDetailedInformation(
    pokemonResponse: PokemonResponse?,
    pokemonSpecies: PokemonSpecies?,
    pokemonTypeWeakness: List<TypesResponse>,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (pokemonResponse != null && pokemonSpecies != null) {
        val scrollState = rememberScrollState()
        Box (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                PokemonInfoTopBar(
                    pokemonName = pokemonResponse.name,
                    pokemonId = pokemonResponse.id,
                    onPreviousClick = onPreviousClick,
                    onNextClick = onNextClick,
                    modifier = modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                PokemonImageComponent(
                    pokemonImageUrl = pokemonResponse.sprites.other.home.front_default,
                    contentDescription = "",
                    modifier = modifier
                        .align(Alignment.CenterHorizontally)
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                PokemonInformation(
                    pokemonResponse = pokemonResponse,
                    pokemonSpecies = pokemonSpecies,
                    pokemonTypeWeakness = pokemonTypeWeakness
                )
                Information7(pokemonResponse = pokemonResponse)
            }
        }

    }
}

@Preview
@Composable
private fun PreviewScreen() {
    PokemonInfoTopBar(
        pokemonName = "Pikachu",
        pokemonId = 1,
        onPreviousClick = { /*TODO*/ },
        onNextClick = { /*TODO*/ })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonInfoTopBar(
    pokemonName: String,
    pokemonId: Int,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pokemonNameUppercase = pokemonName.replaceFirstChar {
        it.uppercase()
    }
    val pokemonIndex = formatNumber(pokemonId)
    val topBarText = pokemonNameUppercase + pokemonIndex

    TopAppBar(
        title = {
            Text(
                text = topBarText,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            IconButton(onClick = onPreviousClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Vorherige")
            }
        },
        actions = {
            IconButton(onClick = onNextClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Nächste")
            }
        },
        modifier = modifier
    )
}

fun formatNumber(number: Int): String {
    val numberString = number.toString()
    if (numberString.length < 4) {
        val leadingZeros = 4 - numberString.length
        val formattedNumber = "0".repeat(leadingZeros) + numberString
        return " #$formattedNumber"
    }
    return " #$numberString"
}

@Composable
fun PokemonImageComponent(
    pokemonImageUrl: String,
    contentDescription: String,
    modifier: Modifier
) {
    Image(
        painter = rememberAsyncImagePainter(pokemonImageUrl),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center,
        modifier = modifier.background(Color.LightGray)
    )
}

@Composable
fun PokemonInformation(
    pokemonResponse: PokemonResponse,
    pokemonSpecies: PokemonSpecies,
    pokemonTypeWeakness: List<TypesResponse>,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Information2(pokemonResponse = pokemonResponse)
        Information1(pokemonResponse = pokemonResponse, pokemonSpecies = pokemonSpecies)
        Information3(pokemonResponse = pokemonResponse, pokemonSpecies = pokemonSpecies)
        Information4(pokemonResponse = pokemonResponse)
        Information5(pokemonResponse = pokemonResponse, pokemonTypeWeakness = pokemonTypeWeakness)
        Information6(pokemonResponse = pokemonResponse)
    }
}

@Composable
fun Information1(pokemonResponse: PokemonResponse, pokemonSpecies: PokemonSpecies, modifier: Modifier = Modifier) {
    val description = getFlavorTexts(pokemonSpecies.flavor_text_entries)
    val formattedDescription = removeLineBreaks(description.first())
    Card (
        modifier = Modifier.background(Color.White)
    ) {
        Text(text = formattedDescription)
    }
}

fun removeLineBreaks(input: String): String {
    //for (item in description) {
    //    formattedDescription = formattedDescription + removeLineBreaks(item) + "\n\n"
    //}
    return input.replace("\n", "").replace("\r", "")
}

fun getFlavorTexts(flavorList: List<FlavorTextEntry>, language: String = "en"): Set<String> {
    val flavorTexts = mutableSetOf<String>()
    for (flavorTextEntry in flavorList) {
        if (flavorTextEntry.language.name == language) {
            val textToAdd = flavorTextEntry.flavor_text
            flavorTexts.add(textToAdd)
        }
    }
    return flavorTexts
}

@Composable
fun Information2(pokemonResponse: PokemonResponse, modifier: Modifier = Modifier) {
    Card (
        modifier = Modifier.background(Color.Gray)
    ) {
        Text(text = "Stats")
        for (stat in pokemonResponse.stats) {
            // TODO stats info /stat/name
            NameWithValueProgressBar(value = stat.base_stat, name = stat.stat.name)
        }
    }


}

@Composable
fun NameWithValueProgressBar(name: String, value: Int) {
    val progress = value.coerceIn(0, 100).toFloat() / 100f

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "$name : $value", modifier = Modifier.padding(bottom = 4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}


@Composable
fun Information3(
    pokemonResponse: PokemonResponse,
    pokemonSpecies: PokemonSpecies,
    modifier: Modifier = Modifier
) {
    val attributeList = listOf(
        "Height" to pokemonResponse.height.toString(),
        "Weight" to pokemonResponse.weight.toString(),
        "Gender" to pokemonResponse.weight.toString(),
        "Category" to pokemonResponse.height.toString(),
        "Abilities" to "Ability"
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .height(200.dp)
            .background(Color.Cyan),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        userScrollEnabled = false
    ) {
        for (attribute in attributeList) {
            item {
                AttributeItem(attributeName = attribute.first, attribute = attribute.second)
            }
        }
    }
}

@Composable
fun AttributeItem(attribute: String, attributeName: String, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .border(
                border = BorderStroke(2.dp, color = Color.Black),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = attributeName, color = Color.White, modifier = modifier)
        Text(text = attribute, color = Color.Black, modifier = modifier)
    }
}

@Composable
fun Information4(pokemonResponse: PokemonResponse, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .background(Color.Yellow)
            .fillMaxWidth()
    ) {
        Text(text = "Type")
        Row (
            horizontalArrangement = Arrangement.Start
        ) {
            for (item in pokemonResponse.types) {
                Text(text = item.type.name)
            }
        }
    }

}

@Composable
fun Information5(pokemonResponse: PokemonResponse, pokemonTypeWeakness: List<TypesResponse>, modifier: Modifier = Modifier) {
    val list = mutableListOf<String>()
    for (weakness in pokemonTypeWeakness) {
        for (item in weakness.damage_relations.double_damage_from) {
            list.add(item.name)
        }
    }
    Card(
        modifier = Modifier.background(Color.Magenta)
            .fillMaxWidth()
    ) {
        Text(text = "Weaknesses")
        Row {
            for (item in list) {
                Text(text = item)
            }
        }
    }

}

@Composable
fun Information6(pokemonResponse: PokemonResponse, modifier: Modifier = Modifier) {
    Text(text = "Evolutions")
}

@Composable
fun Information7(pokemonResponse: PokemonResponse, modifier: Modifier = Modifier) {
    Text(text = "Explore More Pokemon")
}

@Composable
fun VerticalProgressBar(
    statProgress: Int,
    statName: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Green,
    backgroundColor: Color = Color.Black,
    size: Size = Size(width = 10f, height = 150f),
    strokeSize: Float = 1f,
    strokeColor: Color = Color.Blue
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Canvas(
            modifier = modifier
                .background(color = color)
                .size(width = size.width.dp, height = size.height.dp)
                .border(width = strokeSize.dp, color = strokeColor)
        ) {
            // Progress made
            drawRect(
                color = backgroundColor,
                size = Size(width = size.width.dp.toPx(), height = (400f - statProgress).toDp().toPx()),
            )
        }
        Text(text = statName.replace("-", "\n"))
    }

}
