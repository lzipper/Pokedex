package com.example.pokemonapp.model.pokemon

data class PokemonList(
    val results: List<PokemonResult>
)

data class PokemonResult(
    val name: String,
    val url: String
)