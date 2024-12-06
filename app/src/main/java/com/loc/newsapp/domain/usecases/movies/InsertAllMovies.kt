package com.loc.newsapp.domain.usecases.movies

import com.loc.newsapp.data.local.AllMovieDao
import com.loc.newsapp.data.local.MovieDao
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.model.Movie
import javax.inject.Inject

class InsertAllMovies @Inject constructor(
    private val allMovieDao: AllMovieDao

) {
    suspend operator fun invoke(movies: List<Movie>){
        allMovieDao.insertAllMovies(movies = movies)
    }
}