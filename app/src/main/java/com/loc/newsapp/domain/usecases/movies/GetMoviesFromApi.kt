package com.loc.newsapp.domain.usecases.movies

import android.util.Log
import com.loc.newsapp.data.local.AllMovieDao
import com.loc.newsapp.data.remote.MovieApi
import com.loc.newsapp.data.remote.dto.AllMovieResponse
import com.loc.newsapp.data.remote.dto.MovieResponse
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.model.Movie
import com.loc.newsapp.util.Resource
import javax.inject.Inject

class GetMoviesFromApi @Inject constructor(
    private val movieApi: MovieApi,
    private val allMovieDao: AllMovieDao
) {
    suspend fun execute(page: Int): Resource<MovieResponse> {
        return try {

            val response = movieApi.getMovies(page)
            val movieList = response.results.map { result ->
                Movie(
                    id = result.id,
                    poster = result.poster ?: "",
                    title = result.title,
                    overview = result.overview,
                    releaseDate = result.releaseDate,
                    voteAverage = result.voteAverage,
                    originalLanguage = result.originalLanguage
                )
            }.sortedBy { it.id }

            allMovieDao.insertAllMovies(movieList)

            Log.d("GetMoviesFromApi", "Page $page: ${movieList.size} movies loaded.")
            movieList.forEach {
                Log.d("GetMoviesFromApi", "Movie loaded: ${it.title} - ${it.id}")
            }

            Resource.Success(response)
        } catch (e: Exception) {
            Log.e("GetMoviesFromApi", "Failed to load data from API", e)
            Resource.Error("Failed to load data from API: ${e.message}")
        }
    }
}
