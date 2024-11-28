package com.loc.newsapp.domain.usecases.movies

import com.loc.newsapp.data.local.MovieDao
import com.loc.newsapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItems @Inject constructor(
    private val movieDao: MovieDao
) {

    operator fun invoke(): Flow<List<Movie>>{
        return movieDao.getMovies()
    }

}