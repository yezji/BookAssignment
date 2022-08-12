package com.yeji.bookassignment.data

import kotlinx.serialization.Serializable


@Serializable
data class BookData(
    var title: String = "",
    var contents: String = "",
    var url: String = "",
    var isbn: String = "",
    var datetime: String = "",
    var authors: List<String> = listOf(),
    var publisher: String = "",
    var translators: List<String> = listOf(),
    var price: Int = 0,
    var sale_price: Int = 0,
    var thumbnail: String = "",
    var status: String = "",
    var like: Boolean = false
)