package com.loc.newsapp.domain.usecases.movies

import androidx.paging.PagingData
import com.loc.newsapp.domain.model.Movie
import com.loc.newsapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovie @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(): Flow<PagingData<Movie>> {
        return movieRepository.getMovie()
    }
}