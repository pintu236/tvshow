package com.demo.tvshow.data.repository

import com.demo.tvshow.BuildConfig
import com.demo.tvshow.data.dao.TVShowDao
import com.demo.tvshow.data.entity.TVShowEntity
import com.demo.tvshow.data.mapper.RemoteCacheMapper
import com.demo.tvshow.models.PaginationDTO
import com.demo.tvshow.models.TVShowDTO
import com.demo.tvshow.models.TVShowInfoDTO
import com.demo.tvshow.network.IAPIService
import com.demo.tvshow.network.RestClient
import kotlinx.coroutines.flow.Flow

class USTVShowRepositoryImpl(private val tvShowDao: TVShowDao) : TVShowRepository {

    private val defaultLimit: Int = 20
    override suspend fun getSimilarTVShowsFromAPI(seriesId: Int): PaginationDTO<TVShowDTO> {
        return RestClient.create(IAPIService::class.java)
            .getSimilarTVShow(seriesId, BuildConfig.API_KEY)
    }

    override suspend fun getTVShowInfoFromAPI(seriesId: Int): TVShowInfoDTO {
        return RestClient.create(IAPIService::class.java)
            .getTVShowInfo(seriesId, BuildConfig.API_KEY)
    }

    override suspend fun searchTrendingShowsThisWeekFromAPI(query: String): PaginationDTO<TVShowDTO> {

        return RestClient.create(IAPIService::class.java)
            .searchTrendingTVShowThisWeek(BuildConfig.API_KEY, query)
    }

    override suspend fun searchTrendingShowsThisWeekFromCache(query: String): List<TVShowEntity> {
        return tvShowDao.searchTrendingTVShowsThisWeek("%$query%")
    }

    override suspend fun getTrendingShowsThisWeekFromAPI(page: Int): PaginationDTO<TVShowDTO> {
        return RestClient.create(IAPIService::class.java)
            .getTrendingTVShowThisWeek(BuildConfig.API_KEY, page)
    }

    override suspend fun getTrendingShowsThisWeekFromCache(page: Int): List<TVShowEntity> {
        return tvShowDao.getTrendingTVShowsThisWeek(defaultLimit, page)
    }


    override suspend fun getTrendingShowsThisWeek(page: Int): List<TVShowEntity> {
        if (isTrendingShowsThisWeekCached(page)) {
            return getTrendingShowsThisWeekFromCache(page)
        }
        val result = getTrendingShowsThisWeekFromAPI(page)
        //Save
        val savedResult = RemoteCacheMapper.toTVShowEntity(
            result.results, true
        );
        saveTVShows(savedResult)
        return getTrendingShowsThisWeekFromCache(page)
    }

    override fun getTrendingShowsThisWeekFromCacheFlow(): Flow<List<TVShowEntity>> {
        return tvShowDao.getTrendingTVShowsThisWeekFlow()
    }

    override suspend fun searchTrendingShowsThisWeek(query: String): List<TVShowEntity> {
        val result = searchTrendingShowsThisWeekFromCache(query);

        if (result.isNotEmpty()) {
            return result;
        }
        val apiResult = searchTrendingShowsThisWeekFromAPI(query)
        saveTVShows(RemoteCacheMapper.toTVShowEntity(apiResult.results, trending = true))
        return searchTrendingShowsThisWeekFromCache(query)
    }

    override suspend fun isTrendingShowsThisWeekCached(page: Int): Boolean {
        return tvShowDao.isCached(defaultLimit, page)
    }

    override suspend fun saveTVShows(listOfTVShowEntity: List<TVShowEntity>) {
        tvShowDao.upsert(listOfTVShowEntity)
    }

    override suspend fun markFavorite(tvShowId: Int, status: Boolean) {
        return tvShowDao.updateLikeStatus(status, tvShowId)
    }

    override suspend fun isFavorite(tvShowId: Int): Boolean {
        return tvShowDao.isLiked(tvShowId)
    }

}