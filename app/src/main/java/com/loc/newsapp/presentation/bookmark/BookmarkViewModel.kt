package com.loc.newsapp.presentation.bookmark

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.domain.usecases.news.GetItems
import com.loc.newsapp.domain.usecases.news.GetSavedMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val getSavedItems: GetItems
) : ViewModel() {

    private val _state = mutableStateOf(BookmarkState())
    val state: State<BookmarkState> = _state

    init {
        getMovie()
    }

    private fun getMovie() {
        getSavedItems().onEach { items ->
            // Логирование количества элементов
            println("BookmarkViewModel: Received ${items.size} items.")
            _state.value = _state.value.copy(movie = items)
        }.launchIn(viewModelScope)
    }
}