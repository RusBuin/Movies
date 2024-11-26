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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieUseCases: MovieUseCases,
    private val deleteItemsUseCase: DeleteItem,
    private val upsertItemsUseCase: UpsertItem,
    private val getSavedItemUseCase: GetSavedMovie
): ViewModel() {

    var state = mutableStateOf(HomeState())
        private set

    val news = movieUseCases.getMovie(
    ).cachedIn(viewModelScope)
    var sideEffect by mutableStateOf<UIComponent?>(null)
        private set
    fun onEvent (event: DetailsEvent){
        when (event) {
            is DetailsEvent.UpsertDeleteItem ->{
                viewModelScope.launch {
                    val movie = getSavedItemUseCase(id=event.movie.id)
                    if (movie == null){
                        upsertItems(event.movie)
                    }else{
                        deleteItems(event.movie)
                    }
                }
            }
            is DetailsEvent.RemoveSideEffect ->{
                sideEffect = null
            }
        }
    }

    private suspend fun upsertItems(movie: Movie) {
        upsertItemsUseCase(movie)
        sideEffect = UIComponent.Toast("Removed from favorites")
    }

    private suspend fun deleteItems(movie: Movie) {
        deleteItemsUseCase(movie)
        sideEffect = UIComponent.Toast("Added to favorites")

    }

}