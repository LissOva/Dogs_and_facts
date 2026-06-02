package eliza.projects.dogs.ui.screens.random_dog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eliza.projects.dogs.data.model.BreedItem
import eliza.projects.dogs.data.repository.BreedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class RandomDogUiState {
    object Idle : RandomDogUiState()
    object Loading : RandomDogUiState()
    data class Success(val breed: BreedItem) : RandomDogUiState()
    data class Error(val message: String) : RandomDogUiState()
}

class RandomDogViewModel : ViewModel() {
    private val repository = BreedRepository()

    private val _uiState = MutableStateFlow<RandomDogUiState>(RandomDogUiState.Idle)
    val uiState: StateFlow<RandomDogUiState> = _uiState.asStateFlow()

    fun fetchRandomBreed() {
        viewModelScope.launch {
            _uiState.update { RandomDogUiState.Loading }

            when (val result = repository.fetchBreeds()) {
                is BreedRepository.Result.Success -> {
                    // Магия Kotlin: выбираем случайный элемент из списка
                    val randomBreed = result.data.randomOrNull()
                    if (randomBreed != null) {
                        _uiState.update { RandomDogUiState.Success(randomBreed) }
                    } else {
                        _uiState.update { RandomDogUiState.Error("No breeds available") }
                    }
                }

                is BreedRepository.Result.Error -> {
                    _uiState.update {
                        RandomDogUiState.Error(
                            result.exception.message ?: "Unknown error"
                        )
                    }
                }

                else -> {}
            }
        }
    }
}