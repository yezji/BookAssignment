package com.yeji.data.repository

import android.util.Log
import com.yeji.data.model.toDomainModel
import com.yeji.domain.model.BookResponse
import com.yeji.data.repository.remote.ApiRemoteDataSource
import com.yeji.domain.repository.ApiRepository
import com.yeji.domain.repository.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class ApiRepositoryImpl
@Inject constructor(
    private val apiRemoteDataSource: ApiRemoteDataSource
) : ApiRepository {
    override fun getSearchBookListFlow(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
        target: String?
    ): Flow<Result<BookResponse>> {
        return flow {
            emit(Result.Loading) //TODO: 불릴 때 토글해야 하나?????

            val response = apiRemoteDataSource
                .getSearchBookListFlow(query, sort, page, size, target)
                .toDomainModel() // data -> domain

            // success case
            if (response.documents != null) {
                // Flow 블록에서 emit으로 데이터를 발행
                emit(Result.Success(response)) // bookList랑 meta 정보 둘 다 넘기기 위해 response를 넘김
                return@flow
            }
            // failure or error case
            else if (response.errorType != null) {
                emit(Result.Failure(exception = Exception(response.errorType)))
                return@flow
            }
            emit(Result.Failure(exception = NullPointerException()))

        }.catch { e->
            when (e) {
                is HttpException ->
                    Log.e("yezzz repository", "HttpException: ${e.message()}")
                else ->
                    Log.e("yezzz repository", "Throwable: $e")

            }
            emit(Result.Failure(exception = Exception(e.message)))
        }
    }
}