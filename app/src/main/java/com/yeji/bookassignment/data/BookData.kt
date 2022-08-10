package com.yeji.bookassignment.data

import com.google.gson.annotations.SerializedName

data class BookData(
    @SerializedName("title") val title: String,
    @SerializedName("contents") val contents: String,
    @SerializedName("url") val url: String,
    @SerializedName("isbn") val isbn: String,
    @SerializedName("datetime") val datetime: String,
    @SerializedName("authors") val authors: List<String>,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("translators") val translators: List<String>,
    @SerializedName("price") val price: Int,
    @SerializedName("sale_price") val sale_price: Int,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("status") val status: String,
    @SerializedName("like") var like: Boolean
)