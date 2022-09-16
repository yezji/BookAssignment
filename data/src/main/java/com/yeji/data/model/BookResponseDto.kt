package com.yeji.data.model

import com.yeji.domain.model.BookData
import com.yeji.domain.model.BookResponse
import com.yeji.domain.model.MetaData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookResponseDto(
    @SerialName("meta") val meta: MetaData?,
    @SerialName("documents") val documents: List<BookDataDto>?,

    @SerialName("errorType") val errorType: String?,
    @SerialName("message") val message: String?
)
// TODO: BookResponseDto없애기. Dto로 바꾸어주어야 하는 것은 document쪽만 바꾸어야 한다! Response는 그냥 받아야 함
fun BookResponseDto.toDomainModel(): BookResponse {
    return BookResponse(
        meta = this.meta,
        documents = this.documents?.toDomainList(),
        errorType = this.errorType,
        message = this.message
    )
}
