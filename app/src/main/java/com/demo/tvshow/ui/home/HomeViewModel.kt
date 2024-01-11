package com.demo.tvshow.ui.home

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.demo.tvshow.TVShowApplication
import com.demo.tvshow.data.entity.TVShowEntity
import com.demo.tvshow.data.repository.USTVShowRepositoryImpl
import com.demo.tvshow.ui.base.BaseAndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : BaseAndroidViewModel(application) {
    private val originalList = mutableListOf<TVShowEntity>()
    private val _trendingShows = MutableStateFlow<HomeUiState>(HomeUiState.Empty)
    val trendingShows = _trendingShows

    private val repository =
        USTVShowRepositoryImpl((application as TVShowApplication).database.tvShowDao())

    val trendingShowsFlow = repository.getTrendingShowsThisWeekFromCacheFlow()

    var currentPage = 1

    init {
        getTrendingTVShowsThisWeek(currentPage)
    }


    fun getTrendingTVShowsThisWeek(page: Int) {
        viewModelScope.launch {
            try {
                currentPage = page
                _trendingShows.value = HomeUiState.Loading
                val result = repository.getTrendingShowsThisWeek(page)
                if (currentPage == 1) {
                    originalList.clear()
                }
                originalList.addAll(result)
                _trendingShows.value = HomeUiState.Success(originalList, currentPage + 1)
            } catch (e: Exception) {
                _trendingShows.value = HomeUiState.Error(e.message)
            }
        }
    }

    fun searchTrendingTVShowsThisWeek(query: String) {
        viewModelScope.launch {
            try {
                _trendingShows.value = HomeUiState.Loading
                val result = repository.searchTrendingShowsThisWeek(query)
                _trendingShows.value = HomeUiState.Success(result, currentPage + 1)
            } catch (e: Exception) {
                _trendingShows.value = HomeUiState.Error(e.message)
            }
        }
    }
}


// Represents different states for the Home screen
sealed class HomeUiState {

    object Empty : HomeUiState()
    object Loading : HomeUiState()
    data class Success(val tvShows: List<TVShowEntity>, val currentPage: Int) : HomeUiState()
    data class Error(val exception: String?) : HomeUiState()
}