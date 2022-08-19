package com.yeji.bookassignment.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetaData(
    @SerialName("total_count") val total_count: Int?,
    @SerialName("pageable_count") val pageable_count: Int?,
    @SerialName("is_end") val is_end: Boolean?
)
