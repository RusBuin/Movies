package com.loc.newsapp.domain.usecases.movies


import com.loc.newsapp.domain.repository.MovieRepository
import javax.inject.Inject

class LoadMoviesFromDatabase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke() {
        movieRepository.getMoviesFromDatabase()
    }
}
