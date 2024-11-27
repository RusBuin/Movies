package com.loc.newsapp.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.loc.newsapp.domain.model.Movie
import com.loc.newsapp.domain.usecases.movies.DeleteItem
import com.loc.newsapp.domain.usecases.movies.GetSavedMovie
import com.loc.newsapp.domain.usecases.movies.MovieUseCases
import com.loc.newsapp.domain.usecases.movies.UpsertItem
import com.loc.newsapp.presentation.details.DetailsEvent
import com.loc.newsapp.util.UIComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieUseCases: MovieUseCases,
    private val deleteItemsUseCase: DeleteItem,
    private val upsertItemsUseCase: UpsertItem,
    private val getSavedItemUseCase: GetSavedMovie
): ViewModel() {
    val news = movieUseCases.getMovie()
    var state = mutableStateOf(HomeState())
        private set
    var sideEffect by mutableStateOf<UIComponent?>(null)
        private set

    // Используем Map<String, Boolean> для отслеживания закладки каждого фильма
    private val _bookmarkedMovies = mutableStateOf<Map<String, Boolean>>(emptyMap())
    val bookmarkedMovies: Map<String, Boolean> get() = _bookmarkedMovies.value

    fun isMovieBookmarked(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            val movieBookmarked = getSavedItemUseCase(movie.id) != null
            // Обновляем состояние закладки для конкретного фильма
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
            is DetailsEvent.RemoveSideEffect -> {
                sideEffect = null
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

