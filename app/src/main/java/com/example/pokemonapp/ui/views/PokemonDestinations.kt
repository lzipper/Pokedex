package com.example.pokemonapp.ui.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

/**
 * Contract for information needed on every Pokemon navigation destination
 */
interface PokemonDestination {
    val icon: ImageVector
    val route: String
}

object Welcome : PokemonDestination {
    override val icon = Icons.Filled.Add
    override val route = "welcome"
}

object Login : PokemonDestination {
    override val icon = Icons.Filled.Add
    override val route = "login"
}

object PokemonList : PokemonDestination {
    override val icon = Icons.Filled.Add
    override val route = "pokemonList"
}

object PokemonInfo : PokemonDestination {
    override val icon = Icons.Filled.Info
    override val route = "pokemonInfo/{pokemonName}"

    val arguments = listOf(
        navArgument("pokemonName") { type = NavType.StringType }
    )
}

// Screens to be displayed in the top RallyTabRow
val PokemonScreens = listOf(Welcome, Login, PokemonList, PokemonInfo)
