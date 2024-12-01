package com.loc.newsapp.presentation.screens.themeswitcher

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.loc.newsapp.presentation.theme.ThemeOption
import com.loc.newsapp.presentation.theme.ThemeState
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

    }
}