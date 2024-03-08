package com.example.pokemonapp.service

import com.example.pokemonapp.model.pokemon.PokemonResponse
import com.example.pokemonapp.model.pokemon.PokemonList
import com.example.pokemonapp.model.pokemon.StatsResponse
import com.example.pokemonapp.model.pokemon.TypesResponse
import com.example.pokemonapp.model.species.PokemonSpecies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PokemonApiService {
    @GET("pokemon/")
    suspend fun getPokemonList(@Query("limit") limit: Int) : Response<PokemonList>

    @GET("pokemon/{pokemonName}")
    suspend fun getPokemon(@Path("pokemonName") pokemonName: String) : Response<PokemonResponse>

    @GET("pokemon-species/{pokemonName}")
    suspend fun getPokemonSpecies(@Path("pokemonName") pokemonName: String) : Response<PokemonSpecies>

    @GET("type/{pokemonType}")
    suspend fun getTypeWeakness(@Path("pokemonType") pokemonType: String) : Response<TypesResponse>
}