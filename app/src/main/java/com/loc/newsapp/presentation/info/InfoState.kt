package com.loc.newsapp.presentation.info

import android.os.Build
import com.loc.newsapp.BuildConfig

data class InfoState(
    val appVersion: String = BuildConfig.VERSION_NAME,
    val buildNumber: String = BuildConfig.VERSION_CODE.toString(),
    val developerName: String = "Developer Name",
    val developerContact: String = "developer.name@example.com",
    val deviceManufacturer: String = Build.MANUFACTURER,
    val deviceModel: String = Build.MODEL,
    val osVersion: String = Build.VERSION.RELEASE,
    val sdkVersion: String = Build.VERSION.SDK_INT.toString()
)