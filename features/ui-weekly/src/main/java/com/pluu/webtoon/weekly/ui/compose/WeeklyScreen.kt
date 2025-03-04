package com.pluu.webtoon.weekly.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pluu.webtoon.ui.compose.theme.AppTheme
import com.pluu.webtoon.ui_common.R
import com.pluu.webtoon.weekly.event.WeeklyMenuEvent
import com.pluu.webtoon.weekly.model.UI_NAV_ITEM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WeeklyScreen(
    modifier: Modifier = Modifier,
    naviItem: UI_NAV_ITEM,
    onEventAction: (WeeklyMenuEvent) -> Unit,
    drawerState: DrawerState,
    content: @Composable (PaddingValues) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            WeeklyDrawer(
                title = stringResource(R.string.app_name),
                backgroundColor = naviItem.bgColor,
                accentColor = naviItem.color,
                menus = UI_NAV_ITEM.values().iterator(),
                selectedMenu = naviItem
            ) { event ->
                onEventAction(event)
                coroutineScope.launch {
                    drawerState.close()
                }
            }
        },
        modifier = modifier,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                WeeklyTopBar(
                    title = stringResource(R.string.app_name),
                    backgroundColor = naviItem.bgColor,
                    contentColor = Color.White
                ) {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            },
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    widthDp = 320, heightDp = 320
)
@Preview(
    widthDp = 320, heightDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewWeeklyScreen() {
    AppTheme {
        WeeklyScreen(
            drawerState = DrawerState(initialValue = DrawerValue.Open),
            naviItem = UI_NAV_ITEM.NAVER,
            onEventAction = {},
            content = {}
        )
    }
}