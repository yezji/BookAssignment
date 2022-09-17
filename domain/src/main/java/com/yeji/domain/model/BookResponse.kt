package com.yeji.domain.model

data class BookResponse(
    val meta: MetaData?,
    val documents: List<BookData>?,

    val errorType: String?,
    val message: String?
)
