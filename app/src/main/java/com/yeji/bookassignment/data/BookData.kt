package com.yeji.bookassignment.data

import com.google.gson.annotations.SerializedName

data class BookData(
    @SerializedName("title") var title: String = "",
    @SerializedName("contents") var contents: String = "",
    @SerializedName("url") var url: String = "",
    @SerializedName("isbn") var isbn: String = "",
    @SerializedName("datetime") var datetime: String = "",
    @SerializedName("authors") var authors: List<String> = listOf(),
    @SerializedName("publisher") var publisher: String = "",
    @SerializedName("translators") var translators: List<String> = listOf(),
    @SerializedName("price") var price: Int = 0,
    @SerializedName("sale_price") var sale_price: Int = 0,
    @SerializedName("thumbnail") var thumbnail: String = "",
    @SerializedName("status") var status: String = "",
    @SerializedName("like") var like: Boolean = false
)