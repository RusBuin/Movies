package com.loc.newsapp.presentation.screens.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.domain.model.Movie
import com.loc.newsapp.domain.usecases.movies.DeleteItem
import com.loc.newsapp.domain.usecases.movies.GetSavedMovie
import com.loc.newsapp.domain.usecases.movies.UpsertItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val deleteItemsUseCase: DeleteItem,
    private val upsertItemsUseCase: UpsertItem,
    private val getSavedItemUseCase: GetSavedMovie
) : ViewModel() {

    var isBookmarked by mutableStateOf(false)
        private set

    fun isMovieBookmarked(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            isBookmarked = getSavedItemUseCase(movie.id) != null
        }
    }

    fun onEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.UpsertDeleteItem -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val movie = getSavedItemUseCase(event.movie.id)
                    if (movie == null) {
                        upsertItems(event.movie)
                        isBookmarked = true

                    } else {
                        deleteItems(event.movie)
                        isBookmarked = false
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
}