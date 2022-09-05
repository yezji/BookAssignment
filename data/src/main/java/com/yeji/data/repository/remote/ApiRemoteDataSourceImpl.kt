package com.yeji.data.repository.remote

import com.yeji.data.api.remote.BookApiService
import com.yeji.data.model.BookResponseDto
import com.yeji.domain.model.BookResponse
import javax.inject.Inject

/**
 * DataSource에서 선언한 Interface를 구현하는 구현부
 * Api를 통해 Data를 가져오는 것이기에 ApiInterface를 사용한다.
 * DI에서 주입?
 */
class ApiRemoteDataSourceImpl
@Inject constructor(
    private val bookApiService: BookApiService
) : ApiRemoteDataSource {
    override suspend fun getSearchBookListFlow(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
        target: String?
    ) : BookResponseDto {
        return bookApiService.getSearchBookList(
                query = query,
                sort = sort,
                page = page,
                size = size,
                target = target
            )
    }
}