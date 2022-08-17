package com.yeji.bookassignment.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    @SerialName("meta") val meta: MetaData = MetaData(),
    @SerialName("documents") val documents: List<BookData> = mutableListOf(),

    @SerialName("errorType") var errorType: String = "",
    @SerialName("message") var message: String = ""
)
