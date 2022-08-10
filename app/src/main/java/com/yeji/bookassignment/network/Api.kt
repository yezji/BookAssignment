package com.yeji.bookassignment.network

import com.yeji.bookassignment.data.Response
//import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
//    @GET("/v3/search/book")
//    fun getSearchBookList(
//        @Query("query") query: String,
//        @Query("sort") sort: String?,
//        @Query("page") page: Int?,
//        @Query("size") size: Int?,
//        @Query("target") target: String?
//    ) : Single<Response>
    @GET("/v3/search/book")
    suspend fun getSearchBookList(
        @Query("query") query: String,
        @Query("sort") sort: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("target") target: String?
    ) : Response
}