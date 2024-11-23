package com.loc.newsapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.loc.newsapp.domain.model.Movie

data class MovieResponse(
    val page: Int,
    val results: List<Movie>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)