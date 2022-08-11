package com.yeji.bookassignment.data

import com.google.gson.annotations.SerializedName

data class MetaData(
    @SerializedName("total_count") var total_count: Int = 0,
    @SerializedName("pageable_count") var pageable_count: Int = 0,
    @SerializedName("is_end") var is_end: Boolean = true
)