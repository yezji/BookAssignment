package com.yeji.bookassignment.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetaData(
    @SerialName("total_count") var total_count: Int = 0,
    @SerialName("pageable_count") var pageable_count: Int = 0,
    @SerialName("is_end") var is_end: Boolean = true
)
