package com.loc.newsapp.domain.usecases.movies

import com.loc.newsapp.data.local.MovieDao
import com.loc.newsapp.domain.model.Movie
import javax.inject.Inject

class DeleteItem @Inject constructor(
    private val movieDao: MovieDao
) {

    suspend operator fun invoke(movie: Movie) {
        val updatedMovie = movie.copy(isBookmarked = false)
        movieDao.upsert(movie = updatedMovie)
    }
}
