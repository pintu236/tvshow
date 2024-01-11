package com.demo.tvshow.data.repository

import com.demo.tvshow.data.entity.TVShowEntity
import com.demo.tvshow.models.PaginationDTO
import com.demo.tvshow.models.TVShowDTO
import com.demo.tvshow.models.TVShowInfoDTO

interface TVShowRepository {

    suspend fun getSimilarTVShowsFromAPI(seriesId: Int): PaginationDTO<TVShowDTO>

    suspend fun getTVShowInfoFromAPI(seriesId: Int): TVShowInfoDTO

    suspend fun searchTrendingShowsThisWeekFromAPI(query: String): PaginationDTO<TVShowDTO>

    suspend fun searchTrendingShowsThisWeekFromCache(query: String): List<TVShowEntity>

    suspend fun getTrendingShowsThisWeekFromAPI(page: Int): PaginationDTO<TVShowDTO>

    suspend fun getTrendingShowsThisWeekFromCache(page: Int): List<TVShowEntity>

    suspend fun getTrendingShowsThisWeek(page: Int): List<TVShowEntity>

    fun getTrendingShowsThisWeekFromCacheFlow(): kotlinx.coroutines.flow.Flow<List<TVShowEntity>>

    suspend fun searchTrendingShowsThisWeek(query: String): List<TVShowEntity>

    suspend fun isTrendingShowsThisWeekCached(page: Int): Boolean

    suspend fun saveTVShows(listOfTVShowEntity: List<TVShowEntity>)

    suspend fun markFavorite(tvShowId: Int, status: Boolean)

    suspend fun isFavorite(tvShowId: Int): Boolean

}