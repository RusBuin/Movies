package com.loc.newsapp.data.manger

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.loc.newsapp.domain.manger.LocalUserManger
import com.loc.newsapp.presentation.screens.themeswitcher.ThemeOption
import com.loc.newsapp.util.Constants
import com.loc.newsapp.util.Constants.Theme
import com.loc.newsapp.util.Constants.USER_SETTINGS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalUserMangerImpl(
    private val context: Context
) : LocalUserManger {

    override suspend fun saveAppEntry() {
        context.dataStore.edit { settings ->
            settings[APP_ENTRY] = true
        }
    }

    override fun readAppEntry(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[APP_ENTRY] ?: false
        }
    }
    companion object {
        private val APP_ENTRY = booleanPreferencesKey(Constants.APP_ENTRY)
    }


    private val themeKey = stringPreferencesKey("theme")

    override suspend fun getTheme(): ThemeOption {

            val themeString = context.dataStore.data
                .map { preferences -> preferences[themeKey] ?: ThemeOption.SYSTEM_DEFAULT.name }
                .first()
            return ThemeOption.values().find { it.name == themeString } ?: ThemeOption.SYSTEM_DEFAULT
    }

    override suspend fun changeTheme(value: ThemeOption) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = value.name
        }
    }

    override fun readTheme(): Flow<ThemeOption> {
        return context.dataStore.data.map { preferences ->
            val themeString = preferences[themeKey] ?: ThemeOption.SYSTEM_DEFAULT.name
            ThemeOption.values().find { it.name == themeString } ?: ThemeOption.SYSTEM_DEFAULT
        }
    }




}

private val readOnlyProperty = preferencesDataStore(name = USER_SETTINGS)

val Context.dataStore: DataStore<Preferences> by readOnlyProperty








