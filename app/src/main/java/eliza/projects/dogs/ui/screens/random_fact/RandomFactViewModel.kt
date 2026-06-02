package eliza.projects.dogs.ui.screens.random_fact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eliza.projects.dogs.data.repository.BreedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class RandomFactUiState {
    object Idle : RandomFactUiState()
    object Loading : RandomFactUiState()
    data class Success(val fact: String) : RandomFactUiState()
    data class Error(val message: String) : RandomFactUiState()
}

class RandomFactViewModel : ViewModel() {
    private val repository = BreedRepository()

    private val _uiState = MutableStateFlow<RandomFactUiState>(RandomFactUiState.Idle)
    val uiState: StateFlow<RandomFactUiState> = _uiState.asStateFlow()

    fun fetchRandomFact() {
        viewModelScope.launch {
            _uiState.update { RandomFactUiState.Loading }

            when (val result = repository.fetchRandomFact()) {
                is BreedRepository.Result.Success -> {
                    _uiState.update { RandomFactUiState.Success(result.data.attributes.body) }
                }

                is BreedRepository.Result.Error -> {
                    _uiState.update {
                        RandomFactUiState.Error(
                            result.exception.message ?: "Unknown error"
                        )
                    }
                }

                else -> {}
            }
        }
    }
}