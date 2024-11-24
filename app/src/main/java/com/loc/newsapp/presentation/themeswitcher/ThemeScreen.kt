package com.loc.newsapp.presentation.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import com.loc.newsapp.R
import com.loc.newsapp.presentation.Dimens.MediumPadding1

@Composable
fun ThemeScreen(
    state: ThemeState,
    onThemeSelected: (ThemeOption) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(all = MediumPadding1)
    ) {
        Text(
            text = "Theme",
            style = androidx.compose.material3.MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            color = colorResource(id = R.color.text_title)
        )

        Spacer(modifier = Modifier.padding(top = MediumPadding1))

        ThemeOption.values().forEach { themeOption ->
            ThemeOptionItem(
                label = themeOption.displayName,
                isSelected = state.selectedTheme == themeOption,
                onSelect = { onThemeSelected(themeOption) }
            )
        }
    }
}

@Composable
fun ThemeOptionItem(
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MediumPadding1),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = colorResource(id = R.color.text_title),
                unselectedColor = colorResource(id = R.color.text_medium)
            )
        )
        Spacer(modifier = Modifier.padding(horizontal = MediumPadding1))
        Text(
            text = label,
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            color = colorResource(id = R.color.text_title)
        )
    }
}
