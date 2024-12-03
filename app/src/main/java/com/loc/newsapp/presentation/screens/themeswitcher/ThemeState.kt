package com.loc.newsapp.presentation.screens.themeswitcher

data class ThemeState(
    val selectedTheme: ThemeOption = ThemeOption.SYSTEM_DEFAULT
)

enum class ThemeOption(val displayName: String) {
    LIGHT("Light Theme"),
    DARK("Dark Theme"),
    SYSTEM_DEFAULT("System Default")
}
