package com.yeji.bookassignment.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    @SerialName("meta") val meta: MetaData?,
    @SerialName("documents") val documents: List<BookData>?,

    @SerialName("errorType") val errorType: String?,
    @SerialName("message") val message: String?
)
