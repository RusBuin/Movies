package com.loc.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.loc.newsapp.data.local.MovieDao
import com.loc.newsapp.data.local.AllMovieDao
import com.loc.newsapp.data.remote.NewsPagingSource
import com.loc.newsapp.data.remote.MovieApi
import com.loc.newsapp.domain.model.Movie
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class MovieRepositoryImpl(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao,
    private val allMovieDao: AllMovieDao
) : MovieRepository {

    override fun getMovie(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                NewsPagingSource(movieApi = movieApi)
            }
        ).flow
    }

    override suspend fun upsertItems(movie: Movie) {
        movieDao.upsert(movie)
    }

    override suspend fun deleteItems(movie: Movie) {
        movieDao.delete(movie)
    }

    override fun getItems(): Flow<List<Movie>> {
        return movieDao.getMovies()
    }

    override suspend fun upsertAllMovies(movies: List<Movie>) {
        allMovieDao.insertAllMovies(movies)
    }

    override fun getMovieFromLocal(): Flow<List<AllMovie>> {
        return allMovieDao.getAllMoviesFromLocal()
    }

}
