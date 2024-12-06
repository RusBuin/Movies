package com.loc.newsapp.presentation.movies_navigator

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.loc.newsapp.presentation.screens.home.HomeState
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

    var selectedItem by rememberSaveable { mutableStateOf(0) }

    // Выбор текущего экрана на основе backStack
    selectedItem = when (backStackState?.destination?.route) {
        Route.HomeScreen.route -> 0
        Route.BookmarkScreen.route -> 1
        Route.ThemeScreen.route -> 2
        Route.InfoScreen.route -> 3
        else -> 0
    }

    // Скрытие нижней навигации на экране деталей
    val isBottomBarVisible = remember(backStackState) {
        backStackState?.destination?.route in listOf(
            Route.HomeScreen.route,
            Route.BookmarkScreen.route,
            Route.ThemeScreen.route,
            Route.InfoScreen.route
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomBarVisible) {
                NewsBottomNavigation(
                    items = bottomNavigationItems,
                    selectedItem = selectedItem,
                    onItemClick = { index ->
                        navigateToTab(navController, index)
                    }
                )
            }
        }
    ) { paddingValues ->
        val bottomPadding = paddingValues.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(bottom = bottomPadding)
        ) {
            // Экран главной страницы (HomeScreen)
            composable(route = Route.HomeScreen.route) {
                val viewModel: HomeViewModel = hiltViewModel()
                val state = viewModel.state.value
                HomeScreenContent(state, navController, viewModel)
            }

            // Экран деталей (DetailsScreen)
            composable(route = Route.DetailsScreen.route) {
                val viewModel: DetailsViewModel = hiltViewModel()
                val movie = navController.previousBackStackEntry?.savedStateHandle?.get<Movie>("movie")
                movie?.let {
                    DetailsScreen(movie = it, event = viewModel::onEvent, navigateUp = { navController.navigateUp() })
                }
            }

            // Экран закладок (BookmarkScreen)
            composable(route = Route.BookmarkScreen.route) {
                val viewModel: BookmarkViewModel = hiltViewModel()
                BookmarkScreenContent(viewModel, navController)
            }

            // Экран выбора темы (ThemeScreen)
            composable(route = Route.ThemeScreen.route) {
                val viewModel: ThemeViewModel = hiltViewModel()
                val currentTheme by viewModel.currentTheme.collectAsState()
                ThemeScreen(state = ThemeState(selectedTheme = currentTheme)) { themeOption ->
                    viewModel.changeTheme(themeOption)
                }
            }

            // Экран информации (InfoScreen)
            composable(route = Route.InfoScreen.route) {
                val viewModel: InfoViewModel = hiltViewModel()
                InfoScreen(state = viewModel.state.value)
            }
        }
    }
}

@Composable
fun HomeScreenContent(state: HomeState, navController: NavController, viewModel: HomeViewModel) {
    when {
        state.isLoading -> CircularProgressIndicator()
        state.isError -> Text("Ошибка загрузки данных")
        else -> {
            HomeScreen(
                movieFromApi = state.moviesPagingItems, // Передаем LazyPagingItems из состояния
                movieFromLocal = state.movies,           // Локальные фильмы
                navigateToDetails = { movie ->
                    navigateToDetails(navController, movie)
                },
                event = viewModel::onEvent
            )
        }
    }
}


@Composable
fun BookmarkScreenContent(viewModel: BookmarkViewModel, navController: NavController) {
    val state = viewModel.state.value
    OnBackClickStateSaver(navController)
    BookmarkScreen(
        state = state,
        navigateToDetails = { movie ->
            navigateToDetails(navController, movie)
        },
        event = viewModel::onEvent
    )
}

@Composable
fun OnBackClickStateSaver(navController: NavController) {
    BackHandler(true) {
        navigateToTab(navController, 0)
    }
}

private fun navigateToTab(navController: NavController, index: Int) {
    val route = when (index) {
        0 -> Route.HomeScreen.route
        1 -> Route.BookmarkScreen.route
        2 -> Route.ThemeScreen.route
        3 -> Route.InfoScreen.route
        else -> Route.HomeScreen.route
    }
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { screenRoute ->
            popUpTo(screenRoute) { saveState = true }
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun navigateToDetails(navController: NavController, movie: Movie) {
    navController.previousBackStackEntry?.savedStateHandle?.set("movie", movie)
    navController.navigate(Route.DetailsScreen.route)
}
