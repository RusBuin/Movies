package com.loc.newsapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.loc.newsapp.presentation.screens.themeswitcher.ThemeOption

private val DarkColorScheme = darkColorScheme(
    background = Black,
    primary = Blue,
    error = DarkRed,
    surface = LightBlack
)

private val LightColorScheme = lightColorScheme(
    background = Color.White,
    primary = Blue,
    error = LightRed,
    surface = Color.White
)

@Composable
fun NewsAppTheme(
    currentTheme: ThemeOption,
    content: @Composable () -> Unit
) {
    val darkTheme = when (currentTheme) {
        ThemeOption.LIGHT -> false
        ThemeOption.DARK -> true
        ThemeOption.SYSTEM_DEFAULT -> isSystemInDarkTheme()
    }

    val colorScheme = when(currentTheme) {
        ThemeOption.LIGHT-> {
            LightColorScheme
        }
        ThemeOption.DARK -> {
            DarkColorScheme
        }
        else -> {
            if (isSystemInDarkTheme()) {
                DarkColorScheme
            }else{LightColorScheme}
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

