package com.yeji.bookassignment.repository

import com.yeji.bookassignment.data.Response
import com.yeji.bookassignment.network.ApiClient
import com.yeji.bookassignment.network.ApiDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

object ApiRepository {
    private val TAG = ApiRepository::class.java.simpleName

    suspend fun getSearchBookListFlow(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
        target: String?
    ) : Flow<Response> {
        return flow {
            // Flow 블록에서 emit으로 데이터를 발행
            emit(ApiClient.retrofit.getSearchBookList(query, sort, page, size, target))
        }
    }
}