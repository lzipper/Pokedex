package com.example.pokemonapp.model.species

data class FlavorTextEntry(
    val flavor_text: String,
    val language: com.example.pokemonapp.model.species.Language,
    val version: com.example.pokemonapp.model.species.Version
)