package com.pluu.webtoon.episode.ui.compose

import com.pluu.webtoon.model.EpisodeInfo

internal sealed class EpisodeUiEvent {
    object OnShowFirst : EpisodeUiEvent()
    class OnShowDetail(val item: EpisodeInfo) : EpisodeUiEvent()
    object OnBackPressed : EpisodeUiEvent()
}
