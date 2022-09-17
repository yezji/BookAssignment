package com.yeji.domain.model

data class BookData(
    val title: String?,
    val contents: String?,
    val url: String?,
    val isbn: String?,
    val datetime: String?,
    val authors: List<String>?,
    val publisher: String?,
    val translators: List<String>?,
    val price: Double?,
    val sale_price: Int?,
    val thumbnail: String?,
    val status: String?,

    var like: Boolean = false // remote에서 받아온 변수에 한개가 더 필요해서 추가(presentation쪽에서 사용)
)