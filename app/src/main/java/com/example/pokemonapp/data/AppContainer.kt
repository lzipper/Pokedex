package com.example.pokemonapp.data

import retrofit2.Retrofit
import com.example.pokemonapp.service.PokemonApiService
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val pokemonRepository: PokemonRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://pokeapi.com/api/v2/"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        //.addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: PokemonApiService by lazy {
        retrofit.create(PokemonApiService::class.java)
    }

    /**
     * DI implementation for Mars photos repository
     */
    override val pokemonRepository: PokemonRepository by lazy {
        NetworkPokemonInfoRepositoryImpl(retrofitService)
    }
}
