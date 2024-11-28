package com.loc.newsapp.presentation.screens.bookmark

import com.loc.newsapp.domain.model.Movie

data class BookmarkState(
    val movie: List<Movie> = emptyList()
)