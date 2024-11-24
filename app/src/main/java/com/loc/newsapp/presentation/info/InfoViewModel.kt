package com.loc.newsapp.presentation.info

import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.loc.newsapp.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor() : ViewModel() {

    private val _state = mutableStateOf(InfoState())
    val state: State<InfoState> = _state

    init {
        loadInfo()
    }

    private fun loadInfo() {
        _state.value = InfoState(
            appVersion = BuildConfig.VERSION_NAME,
            buildNumber = BuildConfig.VERSION_CODE.toString(),
            developerName = "John Doe",
            developerContact = "john.doe@example.com",
            deviceManufacturer = Build.MANUFACTURER,
            deviceModel = Build.MODEL,
            osVersion = Build.VERSION.RELEASE,
            sdkVersion = Build.VERSION.SDK_INT.toString()
        )
    }
}
