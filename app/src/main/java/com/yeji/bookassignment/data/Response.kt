package com.yeji.bookassignment.data

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("meta") val meta: MetaData,
    @SerializedName("document") val document: List<BookData>
)
