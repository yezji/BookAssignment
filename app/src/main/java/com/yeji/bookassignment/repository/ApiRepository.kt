package com.yeji.bookassignment.repository

import com.yeji.bookassignment.data.Response
import com.yeji.bookassignment.network.ApiFactory
import retrofit2.Call

class ApiRepository {
    companion object {
        private val TAG = ApiRepository::class.java.simpleName
    }

    suspend fun getSearchBookList(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
        target: String?
    ) : Response {
        return ApiFactory.retrofit.getSearchBookList(
            query = query,
            sort = sort,
            page = page,
            size = size,
            target = target
        )
    }
}