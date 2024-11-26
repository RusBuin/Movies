package com.loc.newsapp.domain.usecases.movies

import com.loc.newsapp.domain.repository.MovieRepository

class LoadMoviesFromApi(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke() {
        movieRepository.loadMoviesFromApi()
    }
}
