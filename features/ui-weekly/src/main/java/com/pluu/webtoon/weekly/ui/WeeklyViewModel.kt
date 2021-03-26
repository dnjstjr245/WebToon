package com.pluu.webtoon.weekly.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pluu.utils.AppCoroutineDispatchers
import com.pluu.webtoon.domain.usecase.HasFavoriteUseCase
import com.pluu.webtoon.domain.usecase.WeeklyUseCase
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.model.FavoriteResult
import com.pluu.webtoon.weekly.event.ErrorEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeeklyViewModel @AssistedInject constructor(
    @Assisted val weekPosition: Int,
    private val type: NAV_ITEM,
    private val dispatchers: AppCoroutineDispatchers,
    private val weeklyUseCase: WeeklyUseCase,
    private val hasFavoriteUseCase: HasFavoriteUseCase
) : ViewModel() {

    private val _listEvent = MutableLiveData<List<ToonInfoWithFavorite>>()
    val listEvent: LiveData<List<ToonInfoWithFavorite>> get() = _listEvent

    private val _errorEvent = MutableLiveData<ErrorEvent>()
    val errorEvent: LiveData<ErrorEvent> get() = _errorEvent

    private val _updateEvent: MutableLiveData<FavoriteResult> = MutableLiveData()
    val updateEvent: LiveData<FavoriteResult> get() = _updateEvent

    // Cashing Data
    private val cacheList = mutableListOf<ToonInfo>()
    private val cacheFavorites = mutableSetOf<String>()

    private val ceh = CoroutineExceptionHandler { _, t ->
        _errorEvent.value = ErrorEvent(t.localizedMessage ?: "Unknown Message")
    }

    init {
        viewModelScope.launch {
            // Step1. 주간 웹툰 로드
            val tempList = getWeekLoad(weekPosition)
            // Step2. 즐겨찾기 취득
            cacheFavorites.addAll(getFavorites(tempList))
            // Step3. 즐겨찾기 - 타이틀 순서로 정렬한 값을 리스트로 보관
            cacheList.addAll(
                tempList.sortedWith(compareBy<ToonInfo> {
                    cacheFavorites.contains(it.id).not()
                }.thenBy {
                    it.title
                })
            )
            // Step4. 화면 렌더링
            renderList(cacheList, cacheFavorites)
        }
    }

    private suspend fun getWeekLoad(weekPosition: Int): List<ToonInfo> =
        withContext(dispatchers.computation + ceh) {
            val apiResult: Result<List<ToonInfo>> = weeklyUseCase(weekPosition)
            if (apiResult is Result.Success) {
                apiResult.data
            } else {
                emptyList()
            }
        }

    private suspend fun getFavorites(
        list: List<ToonInfo>
    ): Set<String> = withContext(dispatchers.computation + ceh) {
        list.filter {
            hasFavoriteUseCase(type, it.id)
        }.map {
            it.id
        }.toSet()
    }

    fun updateFavorite(id: String, isFavorite: Boolean) {
        if (isFavorite) {
            cacheFavorites.add(id)
        } else {
            cacheFavorites.remove(id)
        }
        renderList(cacheList, cacheFavorites)
    }

    fun updatedFavorite(favorite: FavoriteResult) {
        _updateEvent.value = favorite
    }

    private fun renderList(
        list: List<ToonInfo>,
        favoriteMap: Set<String>
    ) = viewModelScope.launch {
        _listEvent.value = list
            .map {
                ToonInfoWithFavorite(it, favoriteMap.contains(it.id))
            }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: WeeklyViewModelFactory,
            weekPosition: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(weekPosition) as T
            }
        }
    }
}

@AssistedFactory
interface WeeklyViewModelFactory {
    fun create(weekPosition: Int): WeeklyViewModel
}