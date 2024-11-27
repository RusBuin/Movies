package com.loc.newsapp.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.domain.model.Movie
import com.loc.newsapp.domain.usecases.movies.DeleteItem
import com.loc.newsapp.domain.usecases.movies.GetSavedMovie
import com.loc.newsapp.domain.usecases.movies.UpsertItem
import com.loc.newsapp.util.UIComponent
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

    // Состояние для отслеживания закладок
    var isBookmarked by mutableStateOf(false)
        private set

    // Для отображения временных уведомлений, таких как Toast
    var sideEffect by mutableStateOf<UIComponent?>(null)
        private set

    // Функция для проверки, является ли фильм закладкой
    fun isMovieBookmarked(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            // Проверяем, есть ли фильм в закладках
            isBookmarked = getSavedItemUseCase(movie.id) != null
        }
    }

    // Обработка событий, таких как добавление или удаление из закладок
    fun onEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.UpsertDeleteItem -> {
                viewModelScope.launch(Dispatchers.IO) {
                    // Проверяем, есть ли фильм в закладках
                    val movie = getSavedItemUseCase(event.movie.id)
                    if (movie == null) {
                        // Если фильма нет, добавляем его
                        upsertItems(event.movie)
                        isBookmarked = true
                        sideEffect = UIComponent.Toast("Added to favorites")
                    } else {
                        // Если фильм уже в закладках, удаляем его
                        deleteItems(event.movie)
                        isBookmarked = false
                        sideEffect = UIComponent.Toast("Removed from favorites")
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
}
