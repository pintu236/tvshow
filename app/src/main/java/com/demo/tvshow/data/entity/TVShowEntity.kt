package com.demo.tvshow.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("tv_shows")
data class TVShowEntity(
    @PrimaryKey(false)
    var id: Int,
    @ColumnInfo("adult") var adult: Boolean? = null,
    @ColumnInfo("backdrop_path") var backdropPath: String? = null,
    @ColumnInfo("name") var name: String? = null,
    @ColumnInfo("original_language") var originalLanguage: String? = null,
    @ColumnInfo("original_name") var originalName: String? = null,
    @ColumnInfo("overview") var overview: String? = null,
    @ColumnInfo("poster_path") var posterPath: String? = null,
    @ColumnInfo("media_type") var mediaType: String? = null,
    @ColumnInfo("popularity") var popularity: Double? = null,
    @ColumnInfo("first_air_date") var firstAirDate: String? = null,
    @ColumnInfo("vote_average") var voteAverage: Float? = null,
    @ColumnInfo("vote_count") var voteCount: Int? = null,
    @ColumnInfo("trending") var trendig: Boolean,
    @ColumnInfo("liked") var liked: Boolean = false
)