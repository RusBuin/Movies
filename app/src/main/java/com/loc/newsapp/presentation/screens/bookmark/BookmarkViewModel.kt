package com.loc.newsapp.presentation.screens.bookmark

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.domain.model.Movie
import com.loc.newsapp.domain.usecases.movies.DeleteItem
import com.loc.newsapp.domain.usecases.movies.GetItems
import com.loc.newsapp.domain.usecases.movies.GetSavedMovie
import com.loc.newsapp.domain.usecases.movies.UpsertItem
import com.loc.newsapp.presentation.screens.details.DetailsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val getSavedItems: GetItems,
    private val deleteItemsUseCase: DeleteItem,
    private val upsertItemsUseCase: UpsertItem,
    private val getSavedItemUseCase: GetSavedMovie
) : ViewModel() {

    private val _state = mutableStateOf(BookmarkState())
    val state: State<BookmarkState> = _state

    init {
        getMovie()
    }

    private fun getMovie() {
        getSavedItems().onEach { items ->
            println("BookmarkViewModel: Received ${items.size} items.")
            _state.value = _state.value.copy(movie = items)
        }.launchIn(viewModelScope)
    }

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

        }
    }

    private suspend fun upsertItems(movie: Movie) {
        upsertItemsUseCase(movie)
    }

    private suspend fun deleteItems(movie: Movie) {
        deleteItemsUseCase(movie)

    }

}