package com.loc.newsapp.domain.usecases.movies

import com.loc.newsapp.data.local.AllMovieDao
import com.loc.newsapp.data.local.MovieDao
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieFromLocal @Inject constructor (private val allMovieDao: AllMovieDao) {
    operator fun invoke(): Flow<List<AllMovie>>{
        return allMovieDao.getAllMoviesFromLocal()
    }
}




