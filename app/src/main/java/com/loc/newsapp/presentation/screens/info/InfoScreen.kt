package com.loc.newsapp.presentation.screens.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import com.loc.newsapp.R
import com.loc.newsapp.presentation.Dimens.MediumPadding1
import com.loc.newsapp.presentation.screens.info.InfoState

@Composable
fun InfoScreen(
    state: InfoState,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = MediumPadding1, start = MediumPadding1, end = MediumPadding1)
    ) {
        Text(
            text = "Info",
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            color = colorResource(id = R.color.text_title)
        )

        Spacer(modifier = Modifier.padding(top = MediumPadding1))

        InfoItem("App Version", state.appVersion)
        InfoItem("Build Number", state.buildNumber)
        InfoItem("Developer", state.developerName)
        InfoItem("Contact", state.developerContact)
        InfoItem("Manufacturer", state.deviceManufacturer)
        InfoItem("Model", state.deviceModel)
        InfoItem("OS Version", state.osVersion)
        InfoItem("SDK Version", state.sdkVersion)
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MediumPadding1)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(id = R.color.text_medium)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(id = R.color.text_title)
        )
    }
}
