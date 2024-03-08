package com.example.pokemonapp.ui.views.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pokemonapp.PokemonApplication
import com.example.pokemonapp.model.user.User
import com.example.pokemonapp.model.user.UserRepository
import com.example.pokemonapp.ui.views.pokemonInfoScreen.PokemonInfoViewModel
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUserByEmail(email: String) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            // Hier können Sie die Benutzerdaten weiterverarbeiten, z. B. an die UI übergeben
        }
    }

    fun createUser(email: String, password: String) {
        viewModelScope.launch {
            val user = User(email, password)
            userRepository.createUser(user)
            // Hier können Sie Aktionen ausführen, nachdem der Benutzer erfolgreich erstellt wurde
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PokemonApplication)
                val userRepository = application.container.pokemonRepository
                PokemonInfoViewModel(pokemonRepository = userRepository)
            }
        }
    }

}
