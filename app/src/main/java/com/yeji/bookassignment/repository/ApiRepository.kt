package com.yeji.bookassignment.repository

import android.util.Log
import com.yeji.bookassignment.data.BookData
import com.yeji.bookassignment.data.Response
import com.yeji.bookassignment.network.ApiClient
import com.yeji.bookassignment.network.ApiDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.HttpException

object ApiRepository {
    private val TAG = ApiRepository::class.java.simpleName

    suspend fun getSearchBookListFlow(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
        target: String?
    ) : Flow<Result<List<BookData>>> {
        return flow {
            // Flow 블록에서 emit으로 데이터를 발행
//            try {
//                val response = ApiClient.retrofit.getSearchBookList(query, sort, page, size, target)
//                emit(response)
//            }
//            catch (e: HttpException) {
//            }
//            catch (e: Throwable) {
//            }
            val response = ApiClient.retrofit.getSearchBookList(query, sort, page, size, target)
            if(response.documents != null){
                emit(Result.Success(response.documents))
                return@flow
            }
            emit(Result.Error(exception = NullPointerException()))
        }.catch { e->
            when (e) {
                is HttpException ->
                   Log.e("yezzz repository", "HttpException: ${e.message()}")
                else ->
                    Log.e("yezzz repository", "Throwable: $e")

            }
            emit(Result.Error(exception = Exception(e.message)))
        }
    }
}

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}