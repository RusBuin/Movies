package com.loc.newsapp.presentation.screens.details

import com.loc.newsapp.domain.model.Movie

sealed class DetailsEvent {


    data class UpsertDeleteItem(val movie: Movie) : DetailsEvent()



}