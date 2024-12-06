package com.loc.newsapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.loc.newsapp.domain.model.AllMovie

data class AllMovieResponse(
    val page: Int,
    val results: List<AllMovie>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)
