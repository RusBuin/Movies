package com.loc.newsapp.presentation.mainActivity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.loc.newsapp.presentation.navgraph.NavGraph
import com.loc.newsapp.presentation.screens.themeswitcher.ThemeOption
import com.loc.newsapp.presentation.screens.themeswitcher.ThemeViewModel
import com.loc.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private val themeViewModel by viewModels<ThemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen().apply {
            setKeepOnScreenCondition { mainViewModel.splashCondition.value }
        }

        setContent {
            MainContent(mainViewModel, themeViewModel)
        }
    }
}

@Composable
fun MainContent(mainViewModel: MainViewModel, themeViewModel: ThemeViewModel) {
    val currentTheme by themeViewModel.currentTheme.collectAsState(initial = ThemeOption.SYSTEM_DEFAULT)

    Log.d("MainActivity", "Current Theme: $currentTheme")


    NewsAppTheme(currentTheme = currentTheme) {
        val systemUiController = rememberSystemUiController()
        val isSystemInDarkMode = isSystemInDarkTheme()

        SideEffect {
            val useDarkIcons = when (currentTheme) {
                ThemeOption.LIGHT -> true
                ThemeOption.DARK -> false
                ThemeOption.SYSTEM_DEFAULT -> !isSystemInDarkMode
            }
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons
            )
        }

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            NavGraph(startDestination = mainViewModel.startDestination.value)
        }
    }
}
