

package com.loc.newsapp.presentation.common

import com.loc.newsapp.R
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.loc.newsapp.domain.model.Movie
import com.loc.newsapp.presentation.Dimens.ArticleCardSize
import com.loc.newsapp.presentation.Dimens.ExtraSmallPadding
import com.loc.newsapp.presentation.Dimens.ExtraSmallPadding2
import com.loc.newsapp.presentation.Dimens.SmallIconSize
import com.loc.newsapp.ui.theme.NewsAppTheme


import android.util.Log
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loc.newsapp.domain.model.AllMovie
import com.loc.newsapp.presentation.screens.home.HomeViewModel

@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    movie: Any,  // Параметр типа Any для универсальности
    onClick: (() -> Unit)? = null,
    onBookMarkClick: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    // Проверяем, является ли объект Movie или AllMovie
    val actualMovie = when (movie) {
        is Movie -> movie
        is AllMovie -> Movie(
            id = movie.id,
            poster = movie.poster,
            title = movie.title,
            overview = movie.overview,
            releaseDate = movie.releaseDate,
            voteAverage = movie.voteAverage,
            originalLanguage = movie.originalLanguage
        )
        else -> return  // Если передан неподдерживаемый тип, ничего не делаем
    }

    val context = LocalContext.current
    val isBookmarked = homeViewModel.bookmarkedMovies[actualMovie.id.toString()] ?: false

    LaunchedEffect(Unit) {
        homeViewModel.isMovieBookmarked(actualMovie)
    }

    val imageUrl = "https://image.tmdb.org/t/p/w500${actualMovie.poster}"
    Log.d("MovieCard", "Full Image URL: $imageUrl")

    Row(
        modifier = modifier.clickable { onClick?.invoke() },
    ) {
        AsyncImage(
            modifier = Modifier
                .size(ArticleCardSize)
                .clip(MaterialTheme.shapes.medium),
            model = ImageRequest.Builder(context).data(imageUrl).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(horizontal = ExtraSmallPadding)
                .height(ArticleCardSize)
                .weight(1f)
        ) {
            Text(
                text = actualMovie.title,
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.text_title),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(onClick = onBookMarkClick) {
            Icon(
                painter = painterResource(if (isBookmarked) R.drawable.ic_close
                else R.drawable.ic_bookmark),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
        }
    }
}
