package com.loc.newsapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface AllMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovies(movies: List<Movie>)

    @Query("SELECT * FROM all_movies")
    fun getAllMoviesFromLocal(): Flow<List<AllMovie>>
}