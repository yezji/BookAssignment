package com.yeji.bookassignment.network

import com.yeji.bookassignment.data.BookResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * DataSource에서 선언한 Interface를 구현하는 구현부
 * Api를 통해 Data를 가져오는 것이기에 ApiInterface를 사용한다.
 * DI에서 주입?
 */
class ApiDataSourceImpl: ApiDataSource {
    override fun getSearchBookListFlow(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
        target: String?
    ) : Flow<BookResponse> {
        return flow {
            // Flow 블록에서 emit으로 데이터를 발행
            emit(ApiClient.retrofit.getSearchBookList(query, sort, page, size, target))
        }
    }
}