package com.pluu.webtoon.data

import com.pluu.webtoon.data.repository.LocalRepository
import com.pluu.webtoon.data.repository.RemoteRepository
import com.pluu.webtoon.data.repository.WebToonRepository
import com.pluu.webtoon.model.DetailResult
import com.pluu.webtoon.model.Episode
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.EpisodeResult
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ShareItem
import com.pluu.webtoon.model.Toon
import com.pluu.webtoon.model.ToonId
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.WeekPosition
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class WebToonDataRepository @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : WebToonRepository {
    override fun getFavorites(
        serviceName: String
    ): Flow<Set<ToonId>> = localRepository.getFavorites(serviceName)

    override fun getReadEpisode(
        serviceName: String
        , id: String
    ): Flow<List<Episode>> = localRepository.getReadEpisode(serviceName, id)

    override suspend fun addFavorite(item: Toon) {
        localRepository.addFavorite(item)
    }

    override suspend fun removeFavorite(item: Toon) {
        localRepository.removeFavorite(item)
    }

    override suspend fun readEpisode(item: Episode) {
        localRepository.readEpisode(item)
    }

    override suspend fun getWeekly(weekPosition: WeekPosition): Result<List<ToonInfo>> {
        return remoteRepository.getWeekly(weekPosition)
    }

    override suspend fun getEpisodes(toonId: String, page: Int): Result<EpisodeResult> {
        return remoteRepository.getEpisodes(toonId, page)
    }

    override suspend fun getDetail(
        toonId: ToonId,
        episodeId: EpisodeId,
        episodeTitle: String
    ): DetailResult {
        return remoteRepository.getDetail(toonId, episodeId, episodeTitle)
    }

    override fun getShareItem(
        toonId: ToonId,
        episodeId: EpisodeId,
        episodeTitle: String,
        detailTitle: String,
    ): ShareItem {
        return remoteRepository.getShareItem(toonId, episodeId, episodeTitle, detailTitle)
    }

    override fun getWebToonTabs(): Array<String> {
        return remoteRepository.getWebToonTabs()
    }
}