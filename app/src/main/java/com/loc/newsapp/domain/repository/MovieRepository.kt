package com.loc.newsapp.domain.repository

import androidx.paging.PagingData
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovie(): Flow<PagingData<Movie>>

    suspend fun upsertItems(movie: Movie)

    suspend fun deleteItems(movie: Movie)

    fun getItems(): Flow<List<Movie>>

    suspend fun upsertAllMovies(movies: List<Movie>)
    fun getMovieFromLocal() : Flow<List<AllMovie>>

}