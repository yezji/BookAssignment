package com.yeji.bookassignment.data

import com.google.gson.annotations.SerializedName

data class MetaData(
    @SerializedName("total_count") val total_count: Int,
    @SerializedName("pageable_count") val pageable_count: Int,
    @SerializedName("is_end") val is_end: Boolean
)