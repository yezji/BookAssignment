package com.yeji.data.api.remote

import com.yeji.data.model.BookResponseDto
import com.yeji.domain.model.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {
    @GET("search/book")
    suspend fun getSearchBookList(
        @Query("query") query: String = "가",
        @Query("sort") sort: String? = "accuracy",
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = 50,
        @Query("target") target: String? = "title"
    ) : BookResponseDto
}