package com.yeji.data.repository.remote

import com.yeji.data.model.BookResponseDto

/**
 * DataSource : Local 또는 Remote에서 데이터를 가져오는 쿼리를 담는 역할
 *
 * Api 호출을 통해 Response 데이터를 가져오기 위한 interface
 * DataSourceImpl에서 구현한다.
 */
interface ApiRemoteDataSource {
    suspend fun getSearchBookListFlow(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
        target: String?
    ) : BookResponseDto
}