package com.loc.newsapp.presentation.common

import android.os.Looper
import android.os.Handler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.domain.model.Movie
import com.loc.newsapp.presentation.Dimens.ExtraSmallPadding2
import com.loc.newsapp.presentation.Dimens.MediumPadding1
import com.loc.newsapp.presentation.screens.details.DetailsEvent
@Composable
fun MovieList(
    modifier: Modifier = Modifier,
    movie: List<AllMovie>,
    onClick: (Movie) -> Unit,
    event: (DetailsEvent) -> Unit
) {
    val isWaiting = remember { mutableStateOf(false) }

    if (movie.isEmpty()) {
        EmptyScreen()
        return
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MediumPadding1),
            contentPadding = PaddingValues(all = ExtraSmallPadding2)
        ) {
            itemsIndexed(movie) { index, item ->
                item?.let {
                    val movieItem = Movie(
                        id = it.id,
                        poster = it.poster,
                        title = it.title,
                        overview = it.overview,
                        releaseDate = it.releaseDate,
                        voteAverage = it.voteAverage,
                        originalLanguage = it.originalLanguage
                    )

                    MovieCard(
                        movie = movieItem,
                        onClick = {
                            isWaiting.value = true
                            Handler(Looper.getMainLooper()).postDelayed({
                                isWaiting.value = false
                                onClick(movieItem)
                            }, 80)
                        },
                        onBookMarkClick = {
                            event(DetailsEvent.UpsertDeleteItem(movieItem))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MediumPadding1)
                    )
                }
            }
        }
    }
}
@Composable
fun MovieListBookmark(
    modifier: Modifier = Modifier,
    movie: List<Movie>,
    onClick: (Movie) -> Unit,
    event: (DetailsEvent) -> Unit
) {
    val isWaiting = remember { mutableStateOf(false) }

    if (movie.isEmpty()) {
        EmptyScreen()
        return
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MediumPadding1),
            contentPadding = PaddingValues(all = ExtraSmallPadding2)
        ) {
            itemsIndexed(movie) { index, item ->
                item?.let {
                    val movieItem = Movie(
                        id = it.id,
                        poster = it.poster,
                        title = it.title,
                        overview = it.overview,
                        releaseDate = it.releaseDate,
                        voteAverage = it.voteAverage,
                        originalLanguage = it.originalLanguage
                    )

                    MovieCard(
                        movie = movieItem,
                        onClick = {
                            isWaiting.value = true
                            Handler(Looper.getMainLooper()).postDelayed({
                                isWaiting.value = false
                                onClick(movieItem)
                            }, 80)
                        },
                        onBookMarkClick = {
                            event(DetailsEvent.UpsertDeleteItem(movieItem))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MediumPadding1)
                    )
                }
            }
        }
    }
}


@Composable
fun MovieList(
    modifier: Modifier = Modifier,
    movie: LazyPagingItems<Movie>,
    onClick: (Movie) -> Unit,
    event: (DetailsEvent) -> Unit
) {
    val isWaiting = remember { mutableStateOf(false) }
    val handlePagingResult = handlePagingResult(movie)

    if (handlePagingResult) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MediumPadding1),
            contentPadding = PaddingValues(all = ExtraSmallPadding2)
        ) {
            items(count = movie.itemCount) { index ->
                movie[index]?.let { item ->
                    MovieCard(
                        movie = item,
                        onClick = {
                            isWaiting.value = true
                            Handler(Looper.getMainLooper()).postDelayed({
                                isWaiting.value = false
                                onClick(item)
                            }, 80)
                        },
                        onBookMarkClick = {
                            event(DetailsEvent.UpsertDeleteItem(item))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MediumPadding1)
                    )
                }
            }
        }
    }
}


@Composable
fun handlePagingResult(movie: LazyPagingItems<Movie>): Boolean {
    val loadState = movie.loadState
    val error = when {
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        else -> null
    }

    return when {
        loadState.refresh is LoadState.Loading -> {
            ShimmerEffect()
            false
        }

        error != null -> {
            EmptyScreen(error = error)
            false
        }
        else -> true
    }
}

@Composable
fun ShimmerEffect() {
    Column(verticalArrangement = Arrangement.spacedBy(MediumPadding1)) {
        repeat(10) {
            ArticleCardShimmerEffect(
                modifier = Modifier.padding(horizontal = MediumPadding1)
            )
        }
    }
}

