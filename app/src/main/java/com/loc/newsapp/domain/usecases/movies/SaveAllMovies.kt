package com.loc.newsapp.domain.usecases.movies

import android.util.Log
import com.loc.newsapp.data.remote.MovieApi
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.repository.MovieRepository
import javax.inject.Inject

class SaveAllMovies @Inject constructor(
    private val movieRepository: MovieRepository,
    private val movieApi: MovieApi
) {
    private val TAG = "SaveAllMovies"

    suspend operator fun invoke() {
        var page = 1
        var allMovies = mutableListOf<AllMovie>()
        var totalPages: Int

        Log.d(TAG, "Starting...")

        do {
            try {
                Log.d(TAG, "Page: $page")
                val response = movieApi.getMovies(page)

                val movies = response.results.map { movieResult ->
                    AllMovie(
                        id = movieResult.id,
                        title = movieResult.title,
                        overview = movieResult.overview,
                        poster = movieResult.poster,
                        releaseDate = movieResult.releaseDate,
                        voteAverage = movieResult.voteAverage,
                        originalLanguage = movieResult.originalLanguage,
                        popularity = movieResult.popularity
                    )
                }

                allMovies.addAll(movies)

                movies.forEach {
                    Log.d(TAG, "Film: ${it.title}")
                }

                totalPages = response.totalPages
                page++

            } catch (e: Exception) {
                Log.e(TAG, "Error on $page", e)
                break
            }
        } while (page <= totalPages)

        val sortedMovies = allMovies.sortedByDescending { it.popularity }

        if (sortedMovies.isNotEmpty()) {
            Log.d(TAG, "Saving... ${sortedMovies.size} in local database")
            movieRepository.insertAllMovies(sortedMovies)
        } else {
            Log.d(TAG, "0 films")
        }
    }
}
