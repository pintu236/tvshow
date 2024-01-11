package com.demo.tvshow.data.mapper

import com.demo.tvshow.data.entity.TVShowEntity
import com.demo.tvshow.models.TVShowDTO

object RemoteCacheMapper {

    fun toTVShowEntity(
        tvShows: List<TVShowDTO>,
        trending: Boolean = false
    ): MutableList<TVShowEntity> {
        return tvShows.map {
            TVShowEntity(
                it.id,
                it.adult,
                it.backdropPath,
                it.name,
                it.originalLanguage,
                it.originalName,
                it.overview,
                it.posterPath,
                it.mediaType,
                it.popularity,
                it.firstAirDate,
                it.voteAverage,
                it.voteCount,
                trending
            )
        }.toMutableList()
    }
}