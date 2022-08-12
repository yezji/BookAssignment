package com.yeji.bookassignment.data

import kotlinx.serialization.Serializable

@Serializable
data class MetaData(
    var total_count: Int = 0,
    var pageable_count: Int = 0,
    var is_end: Boolean = true
)