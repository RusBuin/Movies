package com.loc.newsapp.presentation.screens.info

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor() : ViewModel() {

    private val _state = mutableStateOf(InfoState())
    val state: State<InfoState> = _state

}