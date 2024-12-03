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

    private val _currentTheme = MutableStateFlow<ThemeOption>(ThemeOption.SYSTEM_DEFAULT)
    val currentTheme: StateFlow<ThemeOption> = _currentTheme

    init {
        getCurrentTheme()
    }

    private fun getCurrentTheme() {
        viewModelScope.launch {
            val theme = localUserManager.getTheme()
            Log.d("ThemeViewModel", "Loading theme: $theme")
            _currentTheme.emit(theme)
        }
    }

    fun changeTheme(theme: ThemeOption) {
        viewModelScope.launch(Dispatchers.IO) {
            localUserManager.changeTheme(theme)
            Log.d("ThemeViewModel", "Change theme: $theme")
            _currentTheme.emit(theme)
        }
    }
}
