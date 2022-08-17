package com.yeji.bookassignment.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BookData(
    @SerialName("title") var title: String = "",
    @SerialName("contents") var contents: String = "",
    @SerialName("url") var url: String = "",
    @SerialName("isbn") var isbn: String = "",
    @SerialName("datetime") var datetime: String = "",
    @SerialName("authors") var authors: List<String> = listOf(),
    @SerialName("publisher") var publisher: String = "",
    @SerialName("translators") var translators: List<String> = listOf(),
    @SerialName("price") var price: Int = 0,
    @SerialName("sale_price") var sale_price: Int = 0,
    @SerialName("thumbnail") var thumbnail: String = "",
    @SerialName("status") var status: String = "",
    @SerialName("like") var like: Boolean = false
)