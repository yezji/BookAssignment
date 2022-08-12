package com.yeji.bookassignment.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val meta: MetaData = MetaData(),
    val documents: List<BookData> = mutableListOf(),

    var errorType: String = "",
    var message: String = ""
)
