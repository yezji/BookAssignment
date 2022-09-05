package com.yeji.data.model

import com.yeji.domain.model.MetaData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetaDataDto(
    @SerialName("total_count") val total_count: Int?,
    @SerialName("pageable_count") val pageable_count: Int?,
    @SerialName("is_end") val is_end: Boolean?
)

fun MetaDataDto.toDomainModel(): MetaData {
    return MetaData(
        total_count = this.total_count,
        pageable_count = this.pageable_count,
        is_end = this.is_end
    )
}