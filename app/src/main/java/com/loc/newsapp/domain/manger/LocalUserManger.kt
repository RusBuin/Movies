package com.loc.newsapp.domain.manger

import androidx.datastore.preferences.core.Preferences
import com.loc.newsapp.presentation.screens.themeswitcher.ThemeOption
import com.loc.newsapp.util.Constants.Theme
import kotlinx.coroutines.flow.Flow

interface LocalUserManger {

    suspend fun saveAppEntry()

    fun readAppEntry(): Flow<Boolean>
    suspend fun getTheme() : ThemeOption
    suspend fun changeTheme(value: ThemeOption)
    fun readTheme(): Flow<ThemeOption>

}