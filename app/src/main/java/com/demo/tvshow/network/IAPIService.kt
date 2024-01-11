package com.demo.tvshow.network

import com.demo.tvshow.models.PaginationDTO
import com.demo.tvshow.models.TVShowDTO
import com.demo.tvshow.models.TVShowInfoDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IAPIService {
    @GET("tv/{series_id}/similar")
    suspend fun getSimilarTVShow(
        @Path("series_id") seriesId: Int,
        @Query("api_key") apiKey: String
    ): PaginationDTO<TVShowDTO>

    @GET("trending/tv/week?language=en-US")
    suspend fun getTrendingTVShowThisWeek(
        @Query("api_key") apiKey: String, @Query("page") page: Int
    ): PaginationDTO<TVShowDTO>

    @GET("search/tv?language=en-US")
    suspend fun searchTrendingTVShowThisWeek(
        @Query("api_key") apiKey: String, @Query("query") query: String
    ): PaginationDTO<TVShowDTO>

    @GET("tv/{series_id}?language=en-US")
    suspend fun getTVShowInfo(
        @Path("series_id") seriesId: Int,
        @Query("api_key") apiKey: String
    ): TVShowInfoDTO
}