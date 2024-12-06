package com.loc.newsapp.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.model.Movie

class NewsPagingSource(
    private val movieApi: MovieApi
) : PagingSource<Int, Movie>() {


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private var totalNewsCount = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val movieResponse = movieApi.getMovies(page = page)
            totalNewsCount += movieResponse.results.size
            val movies = movieResponse.results.distinctBy { it.title } //Remove duplicates

            LoadResult.Page(
                data = movies,
                nextKey = if (totalNewsCount == movieResponse.totalResults) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(
                throwable = e
            )
        }
    }
}