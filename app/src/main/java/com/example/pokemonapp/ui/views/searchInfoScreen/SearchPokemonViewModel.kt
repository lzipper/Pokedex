package com.example.pokemonapp.ui.views.searchInfoScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pokemonapp.PokemonApplication
import com.example.pokemonapp.data.PokemonRepository
import com.example.pokemonapp.model.pokemon.PokemonResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface PokemonUiState {
    data class Success(val pokemonList: List<PokemonResult>?, val searchText: String) : PokemonUiState
    object Error : PokemonUiState
    object Loading : PokemonUiState
}

class SearchPokemonViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {
    private val  _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _pokemonList = MutableStateFlow<List<PokemonResult>>(emptyList())
    val pokemonList: StateFlow<List<PokemonResult>> = _pokemonList

    /** The mutable State that stores the status of the most recent request */
    var pokemonUiState: PokemonUiState by mutableStateOf(PokemonUiState.Loading)
        private set

    init {
        getPokemonList()
    }

    private fun getPokemonList() {
        viewModelScope.launch {
            pokemonUiState = PokemonUiState.Loading
            pokemonUiState = try {
                val pokemonList = pokemonRepository.getPokemonList()
                _pokemonList.value = pokemonList.body()?.results ?: emptyList()
                val filteredPokemonList = filterPokemonList(pokemonList.body()?.results ?: emptyList(), _searchText.value)
                PokemonUiState.Success(filteredPokemonList, _searchText.value)
            } catch (e: IOException) {
                PokemonUiState.Error
            } catch (e: HttpException) {
                PokemonUiState.Error
            }
        }
    }

    private fun filterPokemonList(pokemonList: List<PokemonResult>, searchText: String): List<PokemonResult> {
        return if (searchText.isNotBlank()) {
            pokemonList.filter { it.name.contains(searchText, ignoreCase = true) }
        } else {
            pokemonList
        }
    }

    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PokemonApplication)
                val pokemonRepository = application.container.pokemonRepository
                SearchPokemonViewModel(pokemonRepository = pokemonRepository)
            }
        }
    }
}
