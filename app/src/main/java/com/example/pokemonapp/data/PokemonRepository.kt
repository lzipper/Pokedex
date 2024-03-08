package com.example.pokemonapp.data

import android.util.Log
import com.example.pokemonapp.model.pokemon.PokemonResponse
import com.example.pokemonapp.model.pokemon.PokemonList
import com.example.pokemonapp.model.pokemon.StatsResponse
import com.example.pokemonapp.model.pokemon.TypesResponse
import com.example.pokemonapp.model.species.PokemonSpecies
import com.example.pokemonapp.service.PokemonApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response


interface PokemonRepository {
    suspend fun getPokemonInfo(pokemon: String): Flow<PokemonResponse>
    suspend fun getPokemon(pokemon: String): Response<PokemonResponse>
    suspend fun getPokemonList(): Response<PokemonList>
    suspend fun getPokemonSpecies(pokemon: String): Response<PokemonSpecies>
    suspend fun getPokemonTypeWeakness(pokemonType: String): Response<TypesResponse>
}


class NetworkPokemonInfoRepositoryImpl(
    private val pokemonInfoService: PokemonApiService
) : PokemonRepository {

    override suspend fun getPokemonList(): Response<PokemonList> {
        return pokemonInfoService.getPokemonList(limit = 9999999)
    }

    override suspend fun getPokemon(pokemon: String): Response<PokemonResponse> {
        return pokemonInfoService.getPokemon(pokemonName = pokemon)
    }

    override suspend fun getPokemonSpecies(pokemon: String): Response<PokemonSpecies> {
        return pokemonInfoService.getPokemonSpecies(pokemonName = pokemon)
    }

    override suspend fun getPokemonTypeWeakness(pokemonType: String): Response<TypesResponse>{
        return pokemonInfoService.getTypeWeakness(pokemonType = pokemonType)
    }




    override suspend fun getPokemonInfo(
        pokemon: String
    ): Flow<PokemonResponse> = flow {
        val response = pokemonInfoService.getPokemon(pokemon)

        if (response.isSuccessful) {
            Log.d("TAG", "Pokemon Info: ${response.body()}")
            val responseBody = response.body();
            if (responseBody != null) {
                emit(responseBody)
            }
        }
    }
}
