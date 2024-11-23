package com.loc.newsapp.presentation.home.components



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


import android.util.Log // добавляем импорт для логирования

@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    movie: Movie,
    onClick: (() -> Unit)? = null
) {
    val context = LocalContext.current

    // Добавляем базовый путь к постеру
    val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster}"
    Log.d("MovieCard", "Full Image URL: $imageUrl") // Логируем полный URL

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
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.text_title),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
