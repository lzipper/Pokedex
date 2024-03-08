package com.example.pokemonapp.model.user

import androidx.compose.runtime.Immutable
import com.example.pokemonapp.data.PokemonRepository
import com.example.pokemonapp.model.pokemon.PokemonList
import com.example.pokemonapp.model.pokemon.PokemonResponse
import com.example.pokemonapp.model.pokemon.TypesResponse
import com.example.pokemonapp.model.species.PokemonSpecies
import com.example.pokemonapp.service.PokemonApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

data class User(
    val email: String,
    val password: String
)

interface UserRepository {
    suspend fun getUserByEmail(email: String): User?
    suspend fun createUser(user: User)
}

class UserRepositoryImpl(
    private val pokemonInfoService: PokemonApiService
) : UserRepository {

    private val users = mutableListOf<User>()

    override suspend fun getUserByEmail(email: String): User? {
        return users.find { it.email == email }
    }

    override suspend fun createUser(user: User) {
        if (getUserByEmail(user.email) == null) {
            users.add(user)
        } else {
            throw IllegalArgumentException("User with email ${user.email} already exists")
        }
    }
}
