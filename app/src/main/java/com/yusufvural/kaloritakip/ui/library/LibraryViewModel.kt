package com.yusufvural.kaloritakip.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufvural.kaloritakip.domain.FoodRepository
import com.yusufvural.kaloritakip.domain.model.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<LibraryUiState>(LibraryUiState.Idle)
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    init {
        _searchQuery
            .debounce(500L)
            .distinctUntilChanged()
            .filter { it.length >= 2 }
            .onEach { query ->
                performSearch(query)
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.length < 2) {
            _uiState.value = LibraryUiState.Idle
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _uiState.value = LibraryUiState.Loading
            repository.searchFood(query)
                .onSuccess { results ->
                    _uiState.value = if (results.isEmpty()) {
                        LibraryUiState.Empty
                    } else {
                        LibraryUiState.Success(results)
                    }
                }
                .onFailure { error ->
                    _uiState.value = LibraryUiState.Error(error.message ?: "Bilinmeyen bir hata oluştu")
                }
        }
    }
}

sealed class LibraryUiState {
    object Idle : LibraryUiState()
    object Loading : LibraryUiState()
    object Empty : LibraryUiState()
    data class Success(val results: List<SearchResult>) : LibraryUiState()
    data class Error(val message: String) : LibraryUiState()
}
