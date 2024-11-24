package com.loc.newsapp.domain.usecases.news

import com.loc.newsapp.data.local.MovieDao
import com.loc.newsapp.domain.model.Movie
import javax.inject.Inject

class GetSavedMovie @Inject constructor(
    private val movieDao: MovieDao

) {
    suspend operator fun invoke(id: Int): Movie?{
        return movieDao.selectItem(id=id)
    }
}