package com.yeji.bookassignment.repo

import com.yeji.bookassignment.network.Api
import com.yeji.bookassignment.network.ApiFactory

class ApiRepository {
    fun getSearchBookList(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
        target: String?
    ) = ApiFactory.retrofit.getSearchBookList(
        query = query,
        sort = sort,
        page = page,
        size = size,
        target = target
    )
}