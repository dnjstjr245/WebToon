package com.pluu.webtoon.data.nate

import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.WeeklyRequest
import com.pluu.webtoon.data.impl.AbstractWeekApi
import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.item.ToonInfo
import com.pluu.webtoon.utils.safeAPi
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * 네이트 웹툰 Week API
 * Created by pluu on 2017-04-26.
 */
class NateWeekApi(
    networkUseCase: NetworkUseCase
) : AbstractWeekApi(networkUseCase, NateWeekApi.TITLE) {

    override fun parseMain(param: WeeklyRequest): Result<List<ToonInfo>> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val apiResult = safeAPi(requestApi(createApi())) { response ->
            Jsoup.parse(response)
        }

        val responseData = when (apiResult) {
            is Result.Success -> apiResult.data
            is Result.Error -> return Result.Error(apiResult.exception)
        }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val pattern = "(?<=btno=)\\d+".toRegex()
        val list = responseData.select(".wkTypeAll_$param")
            .mapNotNull { element ->
                pattern.find(element.attr("href"))?.let { matchResult ->
                    createToon(matchResult.value, element)
                }
            }
        return Result.Success(list)
    }

    private fun createToon(
        id: String,
        element: Element
    ): ToonInfo {
        return ToonInfo(
            id = id,
            title = element.select(".wtl_title").text(),
            image = element.select(".wtl_img img").first().attr("src"),
            writer = element.select(".wtl_author").text()
        )
    }

    private fun createApi(): IRequest = IRequest(
        url = "http://m.comics.nate.com/main/index"
    )

    companion object {
        private val TITLE = arrayOf("월", "화", "수", "목", "금", "토", "일")
    }
}
