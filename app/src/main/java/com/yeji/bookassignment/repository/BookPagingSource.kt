package com.yeji.bookassignment.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yeji.bookassignment.data.BookData
import com.yeji.bookassignment.network.ApiService
import java.lang.Exception

/*

class BookPagingSource(
    val service: ApiService,
    val query: String
) : PagingSource<Int, BookData>() {
    override fun getRefreshKey(state: PagingState<Int, BookData>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookData> {
        try {
            // start refresh at page 1 if undefined
            val nextPageNumber = params.key ?: 1
            val response = service.getSearchBookList(
                query = query,
                sort = "accuracy",
                page = nextPageNumber,
                size = 10,
                target = "title"
            )
            return LoadResult.Page(
//                data = response.documents,
                data = response.,
                prevKey = null,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


}
*/
