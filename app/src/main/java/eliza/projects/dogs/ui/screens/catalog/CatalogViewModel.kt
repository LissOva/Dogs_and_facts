package eliza.projects.dogs.ui.screens.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eliza.projects.dogs.data.model.BreedItem
import eliza.projects.dogs.data.repository.BreedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CatalogUiState(
    val breeds: List<BreedItem> = emptyList(),
    val filteredBreeds: List<BreedItem> = emptyList(),
    val searchQuery: String = "",
    val expandedItemId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class CatalogViewModel : ViewModel() {

    private val repository = BreedRepository()

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        loadBreeds()
    }

    private fun loadBreeds() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = repository.fetchBreeds()) {
                is BreedRepository.Result.Success -> {
                    _uiState.update {
                        it.copy(
                            breeds = result.data,
                            filteredBreeds = result.data,
                            isLoading = false
                        )
                    }
                }

                is BreedRepository.Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = result.exception.message)
                    }
                }

                is BreedRepository.Result.Loading -> {}
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { state ->
            val filtered = state.breeds.filter { breed ->
                breed.attributes.name.contains(query, ignoreCase = true)
            }
            state.copy(
                searchQuery = query,
                filteredBreeds = filtered
            )
        }
    }

    fun toggleExpand(breedId: String) {
        _uiState.update { state ->
            val newExpandedId = if (state.expandedItemId == breedId) null else breedId
            state.copy(expandedItemId = newExpandedId)
        }
    }
}