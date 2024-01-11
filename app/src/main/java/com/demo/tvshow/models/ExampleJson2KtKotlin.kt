package com.demo.tvshow.models

import com.google.gson.annotations.SerializedName

data class PaginationDTO<T>(

    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var results: ArrayList<T> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null

)