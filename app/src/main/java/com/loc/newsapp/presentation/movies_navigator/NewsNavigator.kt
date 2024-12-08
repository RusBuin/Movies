package com.loc.newsapp.presentation.movies_navigator

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.loc.newsapp.R
import com.loc.newsapp.domain.model.Movie
import com.loc.newsapp.presentation.screens.details.DetailsScreen
import com.loc.newsapp.presentation.screens.details.DetailsViewModel
import com.loc.newsapp.presentation.screens.home.HomeViewModel
import com.loc.newsapp.presentation.screens.info.InfoScreen
import com.loc.newsapp.presentation.screens.info.InfoViewModel
import com.loc.newsapp.presentation.navgraph.Route
import com.loc.newsapp.presentation.movies_navigator.components.BottomNavigationItem
import com.loc.newsapp.presentation.movies_navigator.components.NewsBottomNavigation
import com.loc.newsapp.presentation.screens.bookmark.BookmarkScreen
import com.loc.newsapp.presentation.screens.bookmark.BookmarkViewModel
import com.loc.newsapp.presentation.screens.home.HomeScreen
import com.loc.newsapp.presentation.screens.themeswitcher.ThemeScreen
import com.loc.newsapp.presentation.screens.themeswitcher.ThemeState
import com.loc.newsapp.presentation.screens.themeswitcher.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsNavigator() {

    val homeText = stringResource(R.string.icon_home)
    val bookmarkText = stringResource(R.string.icon_bookmark)
    val moodText = stringResource(R.string.icon_mood)
    val infoText = stringResource(R.string.icon_info)

    val bottomNavigationItems = remember {
        listOf(
            BottomNavigationItem(icon = R.drawable.ic_home, text = homeText),
            BottomNavigationItem(icon = R.drawable.ic_bookmark, text = bookmarkText),
            BottomNavigationItem(icon = R.drawable.mode, text = moodText),
            BottomNavigationItem(icon = R.drawable.information, text = infoText)
        )
    }

    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableStateOf(0)
    }
    selectedItem = when (backStackState?.destination?.route) {
        Route.HomeScreen.route -> 0
        Route.BookmarkScreen.route -> 1
        Route.ThemeScreen.route -> 2
        Route.InfoScreen.route -> 3
        else -> 0
    }

    //Hide the bottom navigation when the user is in the details screen
    val isBottomBarVisible = remember(key1 = backStackState) {
        backStackState?.destination?.route == Route.HomeScreen.route ||
                backStackState?.destination?.route == Route.BookmarkScreen.route ||
                backStackState?.destination?.route == Route.ThemeScreen.route ||
                backStackState?.destination?.route == Route.InfoScreen.route
    }


    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        if (isBottomBarVisible) {
            NewsBottomNavigation(
                items = bottomNavigationItems,
                selectedItem = selectedItem,
                onItemClick = { index ->
                    when (index) {
                        0 -> navigateToTab(
                            navController = navController,
                            route = Route.HomeScreen.route
                        )

                        1 -> navigateToTab(
                            navController = navController,
                            route = Route.BookmarkScreen.route
                        )
                        2 -> navigateToTab(
                            navController = navController,
                            route = Route.ThemeScreen.route
                        )
                        3 -> navigateToTab(
                            navController = navController,
                            route = Route.InfoScreen.route
                        )

                    }
                }
            )
        }
    }) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(bottom = bottomPadding)
        ) {
            composable(route = Route.HomeScreen.route) { backStackEntry ->
                val viewModel: HomeViewModel = hiltViewModel()

                // Получаем список фильмов
                val movies = viewModel.movies

                HomeScreen(
                    movies = movies,
                    navigateToDetails = { movie ->
                        navigateToDetails(
                            navController = navController,
                            movie = movie
                        )
                    },
                    event = viewModel::onEvent
                )
            }


            composable(route = Route.DetailsScreen.route) {
                val viewModel: DetailsViewModel = hiltViewModel()
                navController.previousBackStackEntry?.savedStateHandle?.get<Movie?>("movie")
                    ?.let { movie ->
                        DetailsScreen(
                            movie = movie,
                            event = viewModel::onEvent,
                            navigateUp = { navController.navigateUp() }
                        )
                    }

            }
            composable(route = Route.BookmarkScreen.route) {
                val viewModel: BookmarkViewModel = hiltViewModel()
                val state = viewModel.state.value
                OnBackClickStateSaver(navController = navController)
                BookmarkScreen(
                    state = state,
                    navigateToDetails = { movie ->
                        navigateToDetails(
                            navController = navController,
                            movie = movie
                        )
                    },
                    event = viewModel::onEvent
                )
            }
            composable(route = Route.ThemeScreen.route) {
                val viewModel: ThemeViewModel = hiltViewModel()

                // Подписываемся на state, предоставляемый ThemeViewModel
                val currentTheme by viewModel.currentTheme.collectAsState()

                ThemeScreen(
                    state = ThemeState(selectedTheme = currentTheme), // Создаем ThemeState с текущей темой
                    onThemeSelected = { themeOption ->
                        viewModel.changeTheme(themeOption) // Вызываем измененную функцию для изменения темы
                    }
                )
            }


            composable(route = Route.InfoScreen.route) {
                val viewModel: InfoViewModel = hiltViewModel()
                val state = viewModel.state.value
                OnBackClickStateSaver(navController = navController)

                InfoScreen(
                    state = state
                )
            }
        }
    }
}

@Composable
fun OnBackClickStateSaver(navController: NavController) {
    BackHandler(true) {
        navigateToTab(
            navController = navController,
            route = Route.HomeScreen.route
        )
    }
}

private fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { screen_route ->
            popUpTo(screen_route) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun navigateToDetails(navController: NavController, movie: Movie){
    navController.currentBackStackEntry?.savedStateHandle?.set("movie", movie)
    navController.navigate(
        route = Route.DetailsScreen.route
    )
}