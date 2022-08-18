package com.yeji.bookassignment.repository

import com.yeji.bookassignment.data.Response
import com.yeji.bookassignment.network.ApiClient

object ApiRepository {
    private val TAG = ApiRepository::class.java.simpleName

    suspend fun getSearchBookList(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
        target: String?
    ) : Response {
        return ApiClient.retrofit.getSearchBookList(
            query = query,
            sort = sort,
            page = page,
            size = size,
            target = target
        )
    }
}