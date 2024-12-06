package com.loc.newsapp.presentation.screens.home

import androidx.paging.compose.LazyPagingItems
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.model.Movie

data class HomeState(
    val newsTicker: String = "",
    val movies: List<AllMovie> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val moviesPagingItems: LazyPagingItems<Movie>? = null)