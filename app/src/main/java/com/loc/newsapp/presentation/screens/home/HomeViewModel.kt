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
import com.loc.newsapp.domain.usecases.movies.MovieUseCases
import com.loc.newsapp.domain.usecases.movies.SaveAllMovies
import com.loc.newsapp.domain.usecases.movies.UpsertItem
import com.loc.newsapp.presentation.screens.details.DetailsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieUseCases: MovieUseCases,
    private val saveAllMoviesUseCase: SaveAllMovies,
    private val deleteItemsUseCase: DeleteItem,
    private val upsertItemsUseCase: UpsertItem,
    private val getSavedItemUseCase: GetSavedMovie,
    private val getAllMoviesFromLocal: GetAllMoviesFromLocal,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    // Состояние для списка фильмов
    private val _movies = mutableStateOf<List<Movie>>(emptyList())
    val movies: List<Movie> get() = _movies.value

    // Состояние закладок
    private val _bookmarkedMovies = mutableStateOf<Map<String, Boolean>>(emptyMap())
    val bookmarkedMovies: Map<String, Boolean> get() = _bookmarkedMovies.value

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            if (networkHelper.isNetworkAvailable()) {
                // Если есть интернет, загружаем фильмы с сервера и сохраняем в локальную базу
                try {
                    saveAllMoviesUseCase() // Загружаем и сохраняем
                    loadMoviesFromLocal() // Загружаем из базы для отображения
                } catch (e: Exception) {
                    println("Ошибка загрузки фильмов с сервера: ${e.message}")
                    // Если сервер недоступен, показываем локальные данные
                    loadMoviesFromLocal()
                }
            } else {
                // Если интернета нет, сразу показываем локальные фильмы
                loadMoviesFromLocal()
            }
        }
    }

    private fun loadMoviesFromLocal() {
        viewModelScope.launch {
            getAllMoviesFromLocal().collect { movies ->
                // Преобразуем локальные фильмы в общую модель Movie
                _movies.value = movies.map { it.toMovie() }
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
