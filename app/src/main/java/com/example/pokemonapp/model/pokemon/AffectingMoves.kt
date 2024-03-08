package com.example.pokemonapp.model.pokemon

data class AffectingMoves(
    val decrease: List<Decrease>,
    val increase: List<Increase>
)