package com.loc.newsapp.presentation.screens.themeswitcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.domain.manger.LocalUserManger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

import android.util.Log
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val localUserManager: LocalUserManger
) : ViewModel() {

    // Состояние текущей темы
    private val _currentTheme = MutableStateFlow<ThemeOption>(ThemeOption.SYSTEM_DEFAULT)
    val currentTheme: StateFlow<ThemeOption> = _currentTheme.asStateFlow()

    init {
        observeThemeChanges()
    }

    // Подписка на изменения темы
    private fun observeThemeChanges() {
        viewModelScope.launch {
            localUserManager.readTheme().collect { theme ->
                _currentTheme.value = theme
            }
        }
    }

    // Изменение темы
    fun changeTheme(theme: ThemeOption) {
        viewModelScope.launch {
            localUserManager.changeTheme(theme)
        }
    }
}
