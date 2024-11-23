package com.loc.newsapp.domain.usecases.news

import com.loc.newsapp.data.local.MovieDao
import com.loc.newsapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

class GetItems(
    private val movieDao: MovieDao
) {

    operator fun invoke(): Flow<List<Movie>>{
        return movieDao.getItems()
    }

}