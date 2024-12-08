package com.loc.newsapp.presentation.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import com.loc.newsapp.R
import com.loc.newsapp.domain.model.Movie
import com.loc.newsapp.presentation.Dimens.MediumPadding1
import com.loc.newsapp.presentation.common.EmptyScreen
import com.loc.newsapp.presentation.common.MovieList
import com.loc.newsapp.presentation.common.ShimmerEffect
import com.loc.newsapp.presentation.screens.details.DetailsEvent
import com.loc.newsapp.presentation.screens.details.DetailsViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToDetails: (Movie) -> Unit,
    event: (DetailsEvent) -> Unit
) {
    val movies by remember { derivedStateOf { viewModel.movies } }
    val isLoading by remember { derivedStateOf { viewModel.isLoading } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = MediumPadding1)
            .statusBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier
                .width(150.dp)
                .height(30.dp)
                .padding(horizontal = MediumPadding1)
        )

        Spacer(modifier = Modifier.height(MediumPadding1))

        if (isLoading) {
            ShimmerEffect()
        } else {
            MovieList(
                modifier = Modifier.padding(horizontal = MediumPadding1),
                movie = movies,
                onClick = navigateToDetails,
                event = event
            )
        }
    }
}
