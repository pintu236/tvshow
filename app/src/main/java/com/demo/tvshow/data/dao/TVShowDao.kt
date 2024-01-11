package com.demo.tvshow.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.demo.tvshow.data.entity.TVShowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TVShowDao {
    @Upsert
    suspend fun upsert(listOfTVShowEntity: List<TVShowEntity>)

    @Query("SELECT * FROM tv_shows WHERE trending = 1 LIMIT :limit offset (:page-1)*:limit")
    suspend fun getTrendingTVShowsThisWeek(
        limit: Int, page: Int
    ): List<TVShowEntity>

    @Query("SELECT * FROM tv_shows WHERE trending = 1")
    fun getTrendingTVShowsThisWeekFlow(): Flow<List<TVShowEntity>>

    @Query("SELECT EXISTS(SELECT * FROM tv_shows WHERE trending = 1 LIMIT :limit offset (:page-1)*:limit)")
    suspend fun isCached(
        limit: Int, page: Int
    ): Boolean

    @Query("SELECT * FROM tv_shows WHERE trending = 1 AND name LIKE :query")
    suspend fun searchTrendingTVShowsThisWeek(query: String): List<TVShowEntity>

    @Query("UPDATE tv_shows SET liked = :liked WHERE id= :id")
    suspend fun updateLikeStatus(liked: Boolean, id: Int)


    @Query("SELECT EXISTS(SELECT * FROM tv_shows WHERE liked = 1 AND id = :id)")
    suspend fun isLiked(id: Int): Boolean
}