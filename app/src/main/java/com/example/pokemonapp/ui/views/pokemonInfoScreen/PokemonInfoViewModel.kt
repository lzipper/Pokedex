package com.example.pokemonapp.ui.views.pokemonInfoScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pokemonapp.PokemonApplication
import com.example.pokemonapp.data.PokemonRepository
import com.example.pokemonapp.model.pokemon.PokemonResponse
import com.example.pokemonapp.model.pokemon.StatsResponse
import com.example.pokemonapp.model.pokemon.TypesResponse
import com.example.pokemonapp.model.species.PokemonSpecies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonInfoViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {
    private val _pokemonResponse = MutableStateFlow<PokemonResponse?>(null)
    val pokemonResponse: StateFlow<PokemonResponse?> = _pokemonResponse

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error

    private val _pokemonSpecies = MutableStateFlow<PokemonSpecies?>(null)
    val pokemonSpecies: StateFlow<PokemonSpecies?> = _pokemonSpecies

    //private val _pokemonTypeWeakness = MutableStateFlow<TypesResponse?>(null)
    //val pokemonTypeWeakness: StateFlow<TypesResponse?> = _pokemonTypeWeakness

    private val _pokemonWeaknessList = MutableStateFlow<List<TypesResponse>>(emptyList())
    val pokemonWeaknessList: StateFlow<List<TypesResponse>> = _pokemonWeaknessList

    fun getPokemonByName(pokemon: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val pokemonResponse = pokemonRepository.getPokemon(pokemon = pokemon)
                if (pokemonResponse.isSuccessful) {
                    _pokemonResponse.value = pokemonResponse.body() as PokemonResponse
                    _error.value = false
                } else {
                    _error.value = true
                }
            } catch (e: Exception) {
                _error.value = true
            } finally {
                _loading.value = false
            }
        }
    }

    fun getPokemonSpeciesByName(pokemon: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val pokemonSpecies = pokemonRepository.getPokemonSpecies(pokemon = pokemon)
                if (pokemonSpecies.isSuccessful) {
                    _pokemonSpecies.value = pokemonSpecies.body()
                    _error.value = false
                } else {
                    _error.value = true
                }
            } catch (e: Exception) {
                _error.value = true
            } finally {
                _loading.value = false
            }
        }
    }

    suspend fun getAllWeaknessesForPokemon() {
        val weaknesses = mutableListOf<TypesResponse>()
        viewModelScope.launch {
            try {
                _loading.value = true
                pokemonResponse.value?.types?.forEach { type ->
                    val typeResponse = pokemonRepository.getPokemonTypeWeakness(type.type.name)
                    if (typeResponse.body() != null) {
                        if (typeResponse.isSuccessful) {
                            val pokemonType = typeResponse.body() as TypesResponse
                            weaknesses.add(pokemonType)
                            _error.value = false
                        } else {
                            _error.value = true
                        }
                    }
                }
                _pokemonWeaknessList.value = weaknesses
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = true
            } finally {
                _loading.value = false
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PokemonApplication)
                val pokemonRepository = application.container.pokemonRepository
                PokemonInfoViewModel(pokemonRepository = pokemonRepository)
            }
        }
    }
}