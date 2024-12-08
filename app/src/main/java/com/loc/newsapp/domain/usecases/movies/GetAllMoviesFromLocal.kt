package com.loc.newsapp.domain.usecases.movies

import androidx.paging.PagingData
import com.loc.newsapp.data.local.MovieDao
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.model.Movie
import com.loc.newsapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMoviesFromLocal @Inject constructor(
    private val movieDao: MovieDao
) {

    operator fun invoke(): Flow<List<AllMovie>>{
        return movieDao.getAllMovies()
    }

}
