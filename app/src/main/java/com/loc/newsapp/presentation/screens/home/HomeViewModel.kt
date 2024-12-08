package com.loc.newsapp.presentation.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.data.remote.NetworkHelper
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.model.Movie
import com.loc.newsapp.domain.model.toMovie
import com.loc.newsapp.domain.usecases.movies.DeleteItem
import com.loc.newsapp.domain.usecases.movies.GetAllMoviesFromLocal
import com.loc.newsapp.domain.usecases.movies.GetSavedMovie
import com.loc.newsapp.domain.usecases.movies.SaveAllMovies
import com.loc.newsapp.domain.usecases.movies.UpsertItem
import com.loc.newsapp.presentation.screens.details.DetailsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val saveAllMoviesUseCase: SaveAllMovies,
    private val deleteItemsUseCase: DeleteItem,
    private val upsertItemsUseCase: UpsertItem,
    private val getSavedItemUseCase: GetSavedMovie,
    private val getAllMoviesFromLocal: GetAllMoviesFromLocal,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _movies = mutableStateOf<List<Movie>>(emptyList())
    val movies: List<Movie> get() = _movies.value

    private val _isLoading = mutableStateOf(true)
    val isLoading: Boolean get() = _isLoading.value

    private val _bookmarkedMovies = mutableStateOf<Map<String, Boolean>>(emptyMap())
    val bookmarkedMovies: Map<String, Boolean> get() = _bookmarkedMovies.value

    private val _isError = mutableStateOf(false)
    val isError: Boolean get() = _isError.value

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            _isLoading.value = true

            if (networkHelper.isNetworkAvailable()) {
                try {
                    saveAllMoviesUseCase()
                    loadMoviesFromLocal()
                } catch (e: Exception) {
                    _isError.value = true
                    loadMoviesFromLocal()
                }
            } else {
                loadMoviesFromLocal()
            }

            _isLoading.value = false
        }
    }

    private fun loadMoviesFromLocal() {
        viewModelScope.launch {
            getAllMoviesFromLocal().collect { movies ->
                val sortedMovies = movies.map { it.toMovie() }.sortedByDescending { it.popularity }

                _movies.value = sortedMovies
            }
        }
    }


    fun isMovieBookmarked(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            val movieBookmarked = getSavedItemUseCase(movie.id) != null
            _bookmarkedMovies.value = _bookmarkedMovies.value + (movie.id.toString() to movieBookmarked)
        }
    }

    fun onEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.UpsertDeleteItem -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val movie = getSavedItemUseCase(event.movie.id)
                    if (movie == null) {
                        upsertItems(event.movie)
                        updateBookmarkStatus(event.movie.id.toString(), true)
                    } else {
                        deleteItems(event.movie)
                        updateBookmarkStatus(event.movie.id.toString(), false)
                    }
                }
            }
        }
    }

    private suspend fun upsertItems(movie: Movie) {
        upsertItemsUseCase(movie)
    }

    private suspend fun deleteItems(movie: Movie) {
        deleteItemsUseCase(movie)
    }

    private fun updateBookmarkStatus(movieId: String, isBookmarked: Boolean) {
        _bookmarkedMovies.value = _bookmarkedMovies.value + (movieId to isBookmarked)
    }
}
