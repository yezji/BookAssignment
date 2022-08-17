package com.yeji.bookassignment.network

import androidx.paging.PagingData
import com.yeji.bookassignment.data.Response
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
//import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
//    @GET("search/book")
//    fun getSearchBookList(
//        @Query("query") query: String,
//        @Query("sort") sort: String?,
//        @Query("page") page: Int?,
//        @Query("size") size: Int?,
//        @Query("target") target: String?
//    ) : Single<Response>

    @GET("search/book")
    suspend fun getSearchBookList(
        @Query("query") query: String,
        @Query("sort") sort: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("target") target: String?
    ) : Response


//    @GET("search/book")
//    suspend fun getSearchBookListPaging(
//        @Query("query") query: String,
//        @Query("sort") sort: String?,
//        @Query("page") page: Int?,
//        @Query("size") size: Int?,
//        @Query("target") target: String?
//    ) : Flow<PagingData<Response>>
}