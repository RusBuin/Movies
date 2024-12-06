package com.loc.newsapp.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.data.IsNetworkAvailable
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.model.Movie
import com.loc.newsapp.domain.usecases.movies.DeleteItem
import com.loc.newsapp.domain.usecases.movies.GetMovieFromLocal
import com.loc.newsapp.domain.usecases.movies.GetMoviesFromApi
import com.loc.newsapp.domain.usecases.movies.GetSavedMovie
import com.loc.newsapp.domain.usecases.movies.InsertAllMovies
import com.loc.newsapp.domain.usecases.movies.MovieUseCases
import com.loc.newsapp.domain.usecases.movies.UpsertItem
import com.loc.newsapp.presentation.screens.details.DetailsEvent
import com.loc.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMoviesFromApi: GetMoviesFromApi,
    private val deleteItemsUseCase: DeleteItem,
    private val upsertItemsUseCase: UpsertItem,
    private val getSavedItemUseCase: GetSavedMovie,
    private val isNetworkAvailable: IsNetworkAvailable,
    private val getMovieFromLocal: GetMovieFromLocal
) : ViewModel() {

    var state = mutableStateOf(HomeState())
        private set

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true)

            if (isNetworkAvailable()) {
                loadMoviesFromApi()
            } else {
                loadMoviesFromLocal()
            }

            state.value = state.value.copy(isLoading = false)
        }
    }

    private suspend fun loadMoviesFromApi() {
        var currentPage = 1
        var hasMorePages = true
        val allMovies = mutableListOf<AllMovie>()

        while (hasMorePages) {
            val result = getMoviesFromApi.execute(currentPage)
            when (result) {
                is Resource.Success -> {
                    val movies = result.data.results
                    val totalPages = result.data.totalPages

                    val allMovieList = movies.map { movie ->
                        AllMovie(
                            id = movie.id,
                            poster = movie.poster,
                            title = movie.title,
                            overview = movie.overview,
                            releaseDate = movie.releaseDate,
                            voteAverage = movie.voteAverage,
                            originalLanguage = movie.originalLanguage
                        )
                    }

                    allMovies.addAll(allMovieList)
                    hasMorePages = currentPage < totalPages
                }

                is Resource.Error -> {
                    Log.e("HomeViewModel", "Error for page $currentPage: ${result.message}")
                    state.value = state.value.copy(isError = true)
                    hasMorePages = false
                }

                is Resource.Loading -> {
                    Log.d("HomeViewModel", "Loading page $currentPage...")
                }
            }
            currentPage++
        }

        state.value = state.value.copy(
            movies = allMovies,
            isError = false
        )
    }


    private suspend fun loadMoviesFromLocal() {
        getMovieFromLocal().collect { movies ->
            state.value = state.value.copy(movies = movies)
            Log.d("HomeViewModel", "Loading from local.")
        }
    }

    private val _bookmarkedMovies = mutableStateOf<Map<String, Boolean>>(emptyMap())
    val bookmarkedMovies: Map<String, Boolean> get() = _bookmarkedMovies.value

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
