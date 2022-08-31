package com.yeji.bookassignment.network

import com.yeji.bookassignment.data.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("search/book")
    suspend fun getSearchBookList(
        @Query("query") query: String,
        @Query("sort") sort: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("target") target: String?
    ) : BookResponse
}