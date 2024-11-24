package com.loc.newsapp.presentation.details

import com.loc.newsapp.domain.model.Movie

sealed class DetailsEvent {


    data class UpsertDeleteItem(val movie: Movie) : DetailsEvent()

    object RemoveSideEffect : DetailsEvent()


}