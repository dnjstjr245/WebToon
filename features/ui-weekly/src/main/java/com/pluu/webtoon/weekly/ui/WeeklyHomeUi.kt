package com.pluu.webtoon.weekly.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pluu.utils.getSerializable
import com.pluu.utils.toast
import com.pluu.webtoon.Const
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.compose.navigator.LocalEpisodeNavigator
import com.pluu.webtoon.ui.compose.registerStartActivityForResult
import com.pluu.webtoon.ui.model.FavoriteResult
import com.pluu.webtoon.weekly.R
import com.pluu.webtoon.weekly.image.PalletDarkCalculator
import kotlinx.coroutines.launch

@Composable
fun WeeklyHomeUi(
    modifier: Modifier = Modifier,
    viewModelFactory: WeeklyViewModelFactory,
    weekPosition: Int,
) {
    val viewModel: WeeklyViewModel = viewModel(
        key = weekPosition.toString(),
        factory = WeeklyViewModel.provideFactory(viewModelFactory, weekPosition)
    )

    val errorEvent by viewModel.errorEvent.observeAsState()
    val list by viewModel.listEvent.observeAsState(null)
    val updateFavoriteResult by viewModel.updateEvent.observeAsState(null)
    val palletCalculator = PalletDarkCalculator(LocalContext.current)

    val context = LocalContext.current
    val episodeNavigator = LocalEpisodeNavigator.current
    val coroutineScope = rememberCoroutineScope()

    val openEpisodeLauncher = registerStartActivityForResult { activityResult ->
        val favorite = activityResult.data
            ?.getSerializable<FavoriteResult>(Const.EXTRA_FAVORITE_EPISODE)
            ?: return@registerStartActivityForResult
        viewModel.updatedFavorite(favorite)
    }

    LaunchedEffect(errorEvent, updateFavoriteResult) {
        errorEvent?.let { event ->
            context.toast(event.message)
        }
        updateFavoriteResult?.let { favoriteResult ->
            viewModel.updateFavorite(favoriteResult.id, favoriteResult.isFavorite)
        }
    }

    WeeklyHomeUi(
        modifier = modifier,
        items = list,
    ) { item ->
        if (item.info.isLock) {
            context.toast(R.string.msg_not_support)
        } else {
            coroutineScope.launch {
                val palletColor = palletCalculator.calculateSwatchesInImage(item.info.image)
                episodeNavigator.openEpisode(
                    context = context,
                    launcher = openEpisodeLauncher,
                    item = item,
                    palletColor = palletColor
                )
            }
        }
    }
}

@Composable
fun WeeklyHomeUi(
    modifier: Modifier = Modifier,
    items: List<ToonInfoWithFavorite>?,
    onClick: (ToonInfoWithFavorite) -> Unit
) {
    when {
        items == null -> {
            // 초기 Loading
            Box(
                modifier = modifier.wrapContentSize(Alignment.Center)
            ) {
                WeeklyLoadingUi()
            }
        }
        items.isNotEmpty() -> {
            // 해당 요일에 웹툰이 있을 경우
            WeeklyListUi(
                modifier = modifier,
                items = items,
                onClicked = onClick
            )
        }
        else -> {
            // 해당 요일에 웹툰이 없을 경우
            Box(
                modifier = modifier.wrapContentSize(Alignment.Center)
            ) {
                WeeklyEmptyUi()
            }
        }
    }
}
