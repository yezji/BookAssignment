package com.yeji.domain.usecase.book

import com.yeji.domain.model.BookResponse
import com.yeji.domain.repository.ApiRepository
import com.yeji.domain.repository.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookListUseCase @Inject constructor(private val repository: ApiRepository) {
    operator fun invoke(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
        target: String?,
    ) : Flow<Result<BookResponse>> = repository.getSearchBookListFlow(query, sort, page, size, target)
}