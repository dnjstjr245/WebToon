package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.data.dao.IDBHelper
import com.pluu.webtoon.domain.moel.Episode

/**
 * EpisodeInfo Section Use Case
 */
class ReadEpisodeListUseCase(
    private val dbHelper: IDBHelper
) {
    /**
     * 이미 읽은 EpisodeInfo 취득
     *
     * @param type Type
     * @param id EpisodeInfo ID
     * @return Read List
     */
    suspend operator fun invoke(type: NAV_ITEM, id: String): List<Episode> =
        dbHelper.getEpisode(type.name, id)
            .map {
                Episode(
                    service = it.service,
                    toonId = it.toonId,
                    episodeId = it.episodeId
                )
            }
}
