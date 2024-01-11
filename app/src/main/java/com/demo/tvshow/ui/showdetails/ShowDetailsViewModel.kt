package com.demo.tvshow.ui.showdetails

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.demo.tvshow.TVShowApplication
import com.demo.tvshow.data.repository.USTVShowRepositoryImpl
import com.demo.tvshow.models.TVShowDTO
import com.demo.tvshow.models.TVShowInfoDTO
import com.demo.tvshow.ui.base.BaseAndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ShowDetailsViewModel(application: Application) : BaseAndroidViewModel(application) {
    val isFavorite = MutableStateFlow(false)

    val similarTVShows = MutableStateFlow<List<TVShowDTO>>(mutableListOf())

    private val _tvShowInfo = MutableStateFlow<ShowDetailUiState>(ShowDetailUiState.Empty)
    val tvShowInfo = _tvShowInfo
    var currentTVShowInfo: TVShowInfoDTO? = null

    private val repository =
        USTVShowRepositoryImpl((application as TVShowApplication).database.tvShowDao())


    fun getTVShowInfo(id: Int) {
        viewModelScope.launch {
            try {
                _tvShowInfo.value = ShowDetailUiState.Loading
                val result = repository.getTVShowInfoFromAPI(id)
                isFavorite.value = repository.isFavorite(id)
                getSimilarTVShows(id)
                _tvShowInfo.value = ShowDetailUiState.Success(result)
                currentTVShowInfo = result
            } catch (e: Exception) {
                _tvShowInfo.value = ShowDetailUiState.Error(e.message)
            }
        }
    }

    private fun getSimilarTVShows(id: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getSimilarTVShowsFromAPI(id)
                similarTVShows.value = result.results
            } catch (e: Exception) {

            }
        }
    }

    fun markFavorite(id: Int, status: Boolean) {
        viewModelScope.launch {
            try {
                val result = repository.markFavorite(id, status)
                isFavorite.value = status
            } catch (e: Exception) {
                isFavorite.value = false
                _tvShowInfo.value = ShowDetailUiState.Error(e.message)
            }
        }
    }

}


// Represents different states for the Home screen
sealed class ShowDetailUiState {

    object Empty : ShowDetailUiState()
    object Loading : ShowDetailUiState()
    data class Success(val tvShowInfo: TVShowInfoDTO) : ShowDetailUiState()
    data class Error(val exception: String?) : ShowDetailUiState()
}