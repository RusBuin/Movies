package com.loc.newsapp.data.remote

import com.loc.newsapp.data.remote.dto.AllMovieResponse
import com.loc.newsapp.data.remote.dto.MovieResponse
import com.loc.newsapp.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY,
    ): MovieResponse

}

