package com.yeji.domain.repository

import com.yeji.domain.model.BookResponse
import kotlinx.coroutines.flow.*

interface ApiRepository {
    fun getSearchBookListFlow(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
        target: String?
    ) : Flow<Result<BookResponse>>
    // remote에서 받아온 BookResponse를 바로 사용하지 못하기에 data쪽에서는 domain쪽으로 변환할 수 있는 DTO를 만들고, domain쪽에서는 domain의 model을 사용해야 한다?!
}

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}