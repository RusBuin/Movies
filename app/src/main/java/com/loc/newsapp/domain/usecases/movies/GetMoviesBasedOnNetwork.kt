package com.loc.newsapp.domain.usecases.movies

import com.loc.newsapp.data.remote.NetworkHelper
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMoviesBasedOnNetwork @Inject constructor(
    private val saveAllMovies: SaveAllMovies, // Уже существующий юз-кейс
    private val movieRepository: MovieRepository,
    private val networkHelper: NetworkHelper // Для проверки подключения
) {

    suspend operator fun invoke(): Flow<List<AllMovie>> {
        return flow {
            if (networkHelper.isNetworkAvailable()) {
                saveAllMovies()
            }
            emitAll(movieRepository.getAllMovies())
        }
    }
}
