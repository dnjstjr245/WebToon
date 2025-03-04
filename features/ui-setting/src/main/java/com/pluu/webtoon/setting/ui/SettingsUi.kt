package com.pluu.webtoon.setting.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.pluu.compose.ambient.ProvidePreference
import kotlinx.coroutines.launch

@Composable
fun SettingsUi(
    closeCurrent: () -> Unit,
    openLicense: () -> Unit
) {
    SettingsUi(
        viewModel = hiltViewModel(),
        closeCurrent = closeCurrent,
        openLicense = openLicense
    )
}

@Composable
private fun SettingsUi(
    viewModel: SettingsViewModel,
    closeCurrent: () -> Unit,
    openLicense: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        coroutineScope.launch {
            closeCurrent()
        }
    }

    ProvidePreference(viewModel.preferences) {
        SettingsScreen(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            onBackPressed = { closeCurrent() },
            onOpenSourceClicked = { openLicense() }
        )
    }
}