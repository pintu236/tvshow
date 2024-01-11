package com.demo.tvshow.models

import com.google.gson.annotations.SerializedName

data class TVShowDTO(

    @SerializedName("adult") var adult: Boolean? = null,
    @SerializedName("backdrop_path") var backdropPath: String? = null,
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String? = null,
    @SerializedName("original_language") var originalLanguage: String? = null,
    @SerializedName("original_name") var originalName: String? = null,
    @SerializedName("overview") var overview: String? = null,
    @SerializedName("poster_path") var posterPath: String? = null,
    @SerializedName("media_type") var mediaType: String? = null,
    @SerializedName("genre_ids") var genreIds: ArrayList<Int> = arrayListOf(),
    @SerializedName("popularity") var popularity: Double? = null,
    @SerializedName("first_air_date") var firstAirDate: String? = null,
    @SerializedName("vote_average") var voteAverage: Float? = null,
    @SerializedName("vote_count") var voteCount: Int? = null,
    @SerializedName("origin_country") var originCountry: ArrayList<String> = arrayListOf()

)