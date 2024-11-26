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
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    movie: Movie,
    onClick: (() -> Unit)? = null,
    onBookMarkClick: (Boolean) -> Unit // Передаем новое состояние закладки
) {
    val context = LocalContext.current

    // Формируем полный URL для изображения
    val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster}"
    Log.d("MovieCard", "Full Image URL: $imageUrl")

    // Локальное состояние для управления закладкой
    var isBookmarked by remember { mutableStateOf(movie.isBookmarked) }

    // Определяем иконку в зависимости от состояния закладки
    val bookmarkIcon = if (isBookmarked) {
        R.drawable.ic_close // Иконка для фильма в закладках
    } else {
        R.drawable.ic_bookmark // Иконка для фильма не в закладках
    }

    Row(
        modifier = modifier.clickable { onClick?.invoke() },
    ) {
        // Загружаем изображение фильма
        AsyncImage(
            modifier = Modifier
                .size(ArticleCardSize)
                .clip(MaterialTheme.shapes.medium),
            model = ImageRequest.Builder(context).data(imageUrl).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        // Заголовок фильма
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(horizontal = ExtraSmallPadding)
                .height(ArticleCardSize)
                .weight(1f)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.text_title),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Кнопка для добавления в закладки
        IconButton(
            onClick = {
                // Инвертируем состояние закладки
                isBookmarked = !isBookmarked
                // Передаем новое состояние закладки в обработчик
                onBookMarkClick(isBookmarked)
            }
        ) {
            Icon(
                painter = painterResource(id = bookmarkIcon),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
        }
    }
}
