package com.yeji.bookassignment.data

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("meta") val meta: MetaData = MetaData(),
    @SerializedName("documents") val documents: List<BookData> = mutableListOf(),

    @SerializedName("errorType") var errorType: String = "",
    @SerializedName("message") var message: String = ""
)
