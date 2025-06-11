package com.example.myanimelistapp.viewmodel

// file: viewmodel/MovieViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myanimelistapp.data.model.Movie
import com.example.myanimelistapp.repository.MovieRepository
import com.example.myanimelistapp.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState

    init {
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch {
            repository.getPopularMovies()
                .catch { e -> _uiState.value = MovieUiState.Error(e.message ?: "Unknown Error") }
                .collect { result ->
                    when (result) {
                        is Result.Loading -> _uiState.value = MovieUiState.Loading
                        is Result.Success -> _uiState.value = MovieUiState.Success(result.data)
                        is Result.Error -> _uiState.value = MovieUiState.Error(result.message)
                    }
                }
        }
    }
}

// Buat sealed interface untuk merepresentasikan UI State
sealed interface MovieUiState {
    data object Loading : MovieUiState
    data class Success(val movies: List<Movie>) : MovieUiState
    data class Error(val message: String) : MovieUiState
}