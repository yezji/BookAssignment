package com.yeji.bookassignment.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BookData(
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
    @SerialName("status") val status: String?,
    var like: Boolean = false
)