package com.pluu.webtoon.setting.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pluu.utils.toast
import com.pluu.webtoon.setting.ui.LicenseUi
import com.pluu.webtoon.setting.ui.SettingsUi
import com.pluu.webtoon.ui.compose.WebToonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val isDarkTheme = isSystemInDarkTheme()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, !isDarkTheme)
            }

            WebToonTheme(isDarkTheme) {
                Sample()
            }
        }
    }

    private enum class Screen {
        Setting, License
    }

    @Composable
    fun Sample() {
        var screen by remember { mutableStateOf(Screen.Setting) }
        when (screen) {
            Screen.Setting -> {
                Sample_Setting {
                    screen = Screen.License
                }
            }
            Screen.License -> {
                Sample_License {
                    screen = Screen.Setting
                }
            }
        }
    }

    @Composable
    fun Sample_Setting(
        openLicense: () -> Unit
    ) {
        SettingsUi(
            closeCurrent = ::finish,
            openLicense = { openLicense() }
        )
    }

    @Composable
    fun Sample_License(
        closeCurrent: () -> Unit
    ) {
        LicenseUi(
            closeCurrent = { closeCurrent() },
            openBrowser = {
                toast("Show Browser activity")
            }
        )
    }
}