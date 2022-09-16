package com.yeji.data.model

import com.yeji.domain.model.BookData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDataDto(
    @SerialName("title") val title: String?,
    @SerialName("contents") val contents: String?,
    @SerialName("url") val url: String?,
    @SerialName("isbn") val isbn: String?,
    @SerialName("datetime") val datetime: String?,
    @SerialName("authors") val authors: List<String>?,
    @SerialName("publisher") val publisher: String?,
    @SerialName("translators") val translators: List<String>?,
    @SerialName("price") val price: Double?,
    @SerialName("sale_price") val sale_price: Int?,
    @SerialName("thumbnail") val thumbnail: String?,
    @SerialName("status") val status: String?
)

// data쪽의 repositoryImpl 쪽에서 toDomainModel()을 사용하여 domain 모델로 변환해서 넘긴다.
fun BookDataDto.toDomainModel() : BookData {
    return BookData(
        title = this.title,
        contents = this.contents,
        url = url,
        isbn = isbn,
        datetime = this.datetime,
        authors = this.authors,
        publisher = this.publisher,
        translators = this.translators,
        price = this.price,
        sale_price = this.sale_price,
        thumbnail = this.thumbnail,
        status = this.status,

        like = false
    )
}

fun List<BookDataDto>.toDomainList() = map {
    it.toDomainModel()
}