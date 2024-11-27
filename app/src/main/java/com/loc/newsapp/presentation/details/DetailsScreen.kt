package com.loc.newsapp.presentation.details

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.loc.newsapp.R
import com.loc.newsapp.domain.model.Movie
import com.loc.newsapp.presentation.Dimens.ArticleImageHeight
import com.loc.newsapp.presentation.Dimens.MediumPadding1
import com.loc.newsapp.presentation.details.components.DetailsTopBar
import com.loc.newsapp.ui.theme.NewsAppTheme

@Composable
fun DetailsScreen(
    movie: Movie,
    event: (DetailsEvent) -> Unit,
    navigateUp: () -> Unit) {
    val context = LocalContext.current

    val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster}"
    Log.d("DetailsScreen", "Full Image URL: $imageUrl")

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        DetailsTopBar(
            movie = movie,
            onBookMarkClick = {
                event(DetailsEvent.UpsertDeleteItem(movie))
            },
            onBackClick = navigateUp
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = MediumPadding1,
                end = MediumPadding1,
                top = MediumPadding1
            )
        ) {
            item {
                AsyncImage(
                    model = ImageRequest.Builder(context = context).data(imageUrl)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ArticleImageHeight)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(MediumPadding1))
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.displaySmall,
                    color = colorResource(
                        id = R.color.text_title
                    )
                )
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(
                        id = R.color.body
                    )
                )
                Text(
                    text = movie.originalLanguage,
                    style = MaterialTheme.typography.displayMedium,
                    color = colorResource(
                        id = R.color.body
                    )
                )
                Text(
                    text = movie.releaseDate,
                    style = MaterialTheme.typography.labelMedium,
                    color = colorResource(
                        id = R.color.body
                    )
                )
                Text(
                    text = movie.voteAverage.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = colorResource(
                        id = R.color.body
                    )
                )
            }
        }
    }
}
