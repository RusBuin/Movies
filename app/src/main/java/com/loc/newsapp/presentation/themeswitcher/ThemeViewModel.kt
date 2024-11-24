package com.loc.newsapp.presentation.theme

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor() : ViewModel() {

    private val _state = mutableStateOf(ThemeState())
    val state: State<ThemeState> = _state

    fun selectTheme(themeOption: ThemeOption) {
        _state.value = _state.value.copy(selectedTheme = themeOption)
        applyTheme(themeOption)
    }

    private fun applyTheme(themeOption: ThemeOption) {
        // Здесь можно добавить логику сохранения темы (например, через SharedPreferences).
    }
}
